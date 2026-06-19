# Architecture Analysis & Improvement Proposals

## Current State Analysis

### 1. Module Structure
The project is currently divided into several modules:
- `:app`: The main entry point, containing all UI screens (Login, Settings, Champion List, Champion Details) and navigation logic.
- `:domain`: Contains UseCases and Domain Models.
- `:core`: Shared models and utilities used across all modules.
- `:data:network`: Retrofit-based API implementation.
- `:data:storage:impl`: SQLDelight-based local storage implementation.
- `:data:storage:sqldelight`: SQLDelight database and table definitions.

### 2. Dependency Graph Observations
Currently, the dependency flow is:
`app` -> `domain` -> `data:network` & `data:storage:impl`

**Issue:** This is a "data-centric" approach. In strict **Clean Architecture**, the `domain` layer should be the innermost layer and should NOT depend on any data implementation details. Instead, it should define `Repository` interfaces that the `data` layers implement.

### 3. Tight Coupling
- `domain` UseCases (e.g., `ObserveChampion`) directly use classes from `data:storage:impl` (like `ChampionStore`) and `data:storage:sqldelight` (like `ChampionWithFavoritesView`).
- This makes the `domain` layer dependent on the database technology (SQLDelight), violating the principle of being platform/library independent.

### 4. Monolithic App Module
- All feature screens are currently inside the `:app` module. As the application grows, this will lead to:
    - Longer compilation times.
    - Harder to maintain code.
    - Lack of clear boundaries between features.

---

## Proposed Improvements

### 1. Invert Dependencies (True Clean Architecture)
**Goal:** Make `domain` independent of `data`.

- **Action:** Define Repository interfaces in the `:domain` module.
    - Example: `ChampionRepository` interface in `:domain`.
- **Action:** Move the implementations to the `:data` modules.
- **Action:** Update the Gradle dependencies:
    - `:data:network` implementation(project(":domain"))
    - `:data:storage:impl` implementation(project(":domain"))
    - `:app` implementation(project(":domain")), implementation(project(":data:network")), implementation(project(":data:storage:impl"))
- **Action:** Use Hilt to bind the implementations to the interfaces in the `:app` module (or a dedicated `:di` module).

### 2. Feature Modularization
**Goal:** Improve build performance and separation of concerns.

- **Action:** Extract screens into feature modules:
    - `:feature:champions` (List and Details)
    - `:feature:settings`
    - `:feature:auth` (Login)
- **Action:** These feature modules should depend on `:domain` and `:core`.
- **Action:** The `:app` module becomes a thin "glue" module that handles navigation and DI.

### 3. Refine the `:core` Module
**Goal:** Prevent `:core` from becoming a "kitchen sink" module.

- **Action:** Split `:core` into more specific modules if it grows too large:
    - `:core:common`: Pure Kotlin/Java utilities.
    - `:core:ui`: Shared Compose components, themes, and design tokens.
    - `:core:model`: Shared primitive models (e.g., `ChampionId`).

### 4. Data Layer Abstraction
**Goal:** Isolate storage implementation details.

- **Action:** Ensure that `:data:storage:impl` maps its internal database entities (SQLDelight generated classes) to domain models before passing them to the repository implementation.
- **Action:** Use the `Result` pattern or a custom wrapper for network calls in the repository implementation to handle errors gracefully before they reach the UseCases.

### 5. Navigation Decoupling
**Goal:** Make features truly independent.

- **Action:** Use a "Feature API / Feature Implementation" pattern or a "Coordinator" pattern to allow features to navigate to each other without depending on each other directly.

---

## Summary of Benefits
1.  **Testability**: Domain logic can be unit tested without any Android or database dependencies.
2.  **Scalability**: Multiple developers can work on different feature modules simultaneously with fewer merge conflicts.
3.  **Build Speed**: Gradle can parallelize the build of independent modules and only recompile changed modules.
4.  **Flexibility**: Easier to swap the database (e.g., from SQLDelight to Room) or the network client without touching the business logic.
