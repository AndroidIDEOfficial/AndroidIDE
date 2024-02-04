# Test resources

This directory contains resources for unit tests related to the tooling API server, the projects API and LSP modules.

- `test-project`: A basic project used for testing almost everything related to the tooling API
  (multi-module support, build cancellations, dependency & task resolutions, model builders, etc.).
  This project is not buildable i.e. running `assembleDebug` in this project will fail.
- `sample-project`: A basic project which can be built, used in some rare cases (like testing configuration cache support).