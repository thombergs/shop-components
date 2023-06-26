# Decision records

**Note:** The decision records below are just an example of how decisions could have been made during the initial development of a component-based architecture. It doesn't mean that the decisions below are the right decisions for every case. You can and should deviate from those decisions in your own projects (if you have good reasons to).

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

## 002 - Expose narrow use cases in the component API

### What?
Each component API exposes methods for narrowly defined use cases. The clients don't need to work with the domain model to change the model's state. Instead, they just call the use case methods.  

### Why?
- Clients don't need to orchestrate calls to the domain model and then pass the domain model into the right service methods to persist that state. They only need to know the use case methods.

## 003 - Expose the domain model in the component API

### What?
The `api` packages of our top-level components expose the domain model. 

### Why?
- The domain model is (currently) rather anemic, so there is not much domain logic to protect from clients (and even if a client calls a state-changing method on a domain model, it won't be persisted, because state can only be changed through the use case API - see decision 002).
- This way, we don't need to add an extra mapping layer to map the domain model to an external representation. The clients of our component APIs can map it into whatever form it needs.

## 004 - Initialize database state for tests via code

### What?
In integration tests, we use the real database (started in a Docker container). To initialize state in that test database, we use code that uses our database components. This will require us to provide some methods in the database subcomponents' interface that we only need for testing. 

### Why?
- Initializing the database via code tests the database component more comprehensively.
- Using code to initialize the database makes the initialization more flexible than, say, importing certain SQL scripts. We can re-use certain init methods in different tests.
- Code is easier to refactor and keep up-to-date than SQL scripts.

## 005 - Separate the database model from the domain model

### What?
The domain model needs to be persisted in the database. We could use Spring Data annotations on the domain model classes and use that for persistence. Instead, we're creating a domain model that is completely independent of the persistence mechanism and, in the database subcomponent, we create a separate database model that is used to map the domain model into the database.

### Why?
- We don't have to care about persistence specifics in the domain code (for example, Spring Data JDBCs' `@MappedCollection` to map 1-to-many relationships).
- We can switch the persistence mechanism without affecting the domain model (I have switched from Spring Data to JOOQ, once, for example, because the queries grew more dynamic over time).


## 006 - Use Spring for application events

### What?

To send events between components, we use Spring's event bus (in a synchronous way).

### Why?

- Spring's event bus is the easiest way to exchange events. We don't have to set up a separate event broker and still can have the components loosely coupled at compile time.
- Since the event publishers and listeners are implemented as their own subcomponents, we can later easily switch to a scalable event mechanism (SQS, RabbitMQ, ActiveMQ, ...) should this be required (we just need to be aware that we're switching from sync to async event handling, then!).

## 007 - Components don't share model classes

### What?
Components don't share the same classes for input, output, or database models. Instead, even if a data structure is the same, they have their own copy of the data structure and map between them.

_Components **are** allowed to access each others' `api` classes_, however that should be done sparingly to avoid unintentionally tight coupling.

### Why?
- If components share model classes, they quickly become tightly coupled and cannot evolve separately anymore, which is the main reason we create boundaries between them in the first place.

## 008 - Components share the same database schema (for now)

### What?
Even though each component's database model is kept completely separate from each other in the code (each hidden in its own `database` subcomponent that is not accessible outside of the parent component), the components share the same database schema initially.

### Why?
- A single database schema per application is the easiest to set up and get started.
- Since the database code between components is not coupled at all, it's rather easy to evolve into separate schemas or even separate databases in the future.