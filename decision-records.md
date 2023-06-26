# Decision records

## 001 - Component-based architecture

### What?
Each "subdomain" is encapsulated in a clearly defined "component".

- Each component has a dedicated namespace (package name).
- Each component has a dedicated `api` package that is accessible to the outside world.
- Each component has a dedicated `internal` package that is not accessible to the outside world.
- A component may contain subcomponents in its `internal` package.

### Why?
- With each component having a clear entrypoint (the `api` package), we have clearly defined boundaries between components.
- Clear boundaries mean higher maintainability over time because of fewer unwanted dependencies.
- Clear boundaries mean that the subdomains can evolve largely separately from each other (and even developed by different teams).
- We can defend against unwanted dependencies by a simple ArchUnit rule that fails when a package named `internal` is accessed from the outside.

## 002 - Exposing narrow use cases in the component API

### What?
Each component API exposes methods for narrowly defined use cases. The clients don't need to work with the domain model to change the model's state. Instead, they just call the use case methods.  

### Why?
- Clients don't need to orchestrate calls to the domain model and then pass the domain model into the right service methods to persist that state. They only need to know the use case methods.

## 003 - Exposing the domain model in the component API

### What?
The `api` packages of our top-level components expose the domain model. 

### Why?
- The domain model is (currently) rather anemic, so there is not much domain logic to protect from clients (and even if a client calls a state-changing method on a domain model, it won't be persisted, because state can only be changed through the use case API - see decision 002).
- This way, we don't need to add an extra mapping layer to map the domain model to an external representation. The clients of our component APIs can map it into whatever form it needs.

## 004 - Initialize database state for tests via code

... and not via pre-defined SQL scripts
... means that we might have to declare API methods in the database sub-components only for testing to add data to the database

## 005 - Separate the database model from the domain model
