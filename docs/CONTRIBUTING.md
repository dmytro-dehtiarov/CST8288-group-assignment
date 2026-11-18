# Contributing

## Getting started

Clone the repository and enter the project directory:

```bash
git clone https://github.com/dmytro-dehtiarov/CST8288-group-assignment.git
cd CST8288-group-assignment
```

Make sure Java 21 is installed. Maven does not need to be installed because the project includes Maven Wrapper.

Run the tests:

```bash
./mvnw test
```

On Windows, use:

```bat
mvnw.cmd test
```

## Database

The application uses the course MySQL container on `localhost:3307` with user `cst8288` and
password `cst8288` (see `src/main/resources/database.properties`).

Create or reset the `ckc_db` database with the schema and sample data:

```bash
docker exec -i mysql mysql -ucst8288 -pcst8288 < db/ckc.sql
```

The script drops and recreates `ckc_db`, so run it again after anyone changes `db/ckc.sql`.

Get connections from `DataSource.INSTANCE.getConnection()`. **Do not close this connection**:
`DataSource` shares one connection and manages it. Close only your `PreparedStatement` and
`ResultSet`, for example:

```java
Connection conn = DataSource.INSTANCE.getConnection();
try (PreparedStatement ps = conn.prepareStatement("SELECT ... WHERE id = ?")) {
    ps.setInt(1, id);
    try (ResultSet rs = ps.executeQuery()) {
        // read rows
    }
}
```

## Tests

There are two kinds of tests:

- **Unit tests** check business rules and design patterns without a database. Give business
  classes a fake DAO (a small test class that implements the DAO interface) instead of the real one.
  Unit tests must never use `DataSource`.
- **Database tests** check DAO classes against a real MySQL. Make the test class extend
  `DatabaseTest`. It starts one MySQL container for the whole test run, loads `db/ckc.sql` into it
  and points `DataSource` at it, so you do not configure anything yourself. See `DataSourceTest`
  for an example.

Database tests need Docker running. Without Docker they are skipped, not failed. The first run
downloads the MySQL image and takes longer.

Before each database test, `DatabaseTest` empties every table, so each test starts with an empty
database. Insert the rows your test needs at the start of the test. The sample data from
`db/ckc.sql` is for the demo, not for tests.

Also you can use `@BeforeEach` annotation if you need same sample data in each test

Run all tests before you open a Pull Request:

```bash
./mvnw clean verify
```

## Creating a change

Start from an up-to-date `main` branch:

```bash
git switch main
git pull origin main
git switch -c feat/short-description
```

Make your changes, then run the tests again:

```bash
./mvnw test
```

Use a clear Conventional Commit message:

```bash
git add .
git commit -m "feat: add short description"
```

Push the branch and open a Pull Request:

```bash
git push -u origin feat/short-description
```

Pull Requests should target `main`. Wait for the CI check to pass and request a review before merging.

## Branch naming

Use lowercase names with a type prefix and hyphens between words:

```text
<type>/<short-description>
```

Examples:

```text
feat/user-registration
fix/order-validation
test/order-service
docs/update-contributing-guide
build/update-maven-config
ci/improve-build-workflow
chore/cleanup-project-files
```

Keep branch names short and describe one task. Do not use spaces, uppercase letters, or vague names such as `changes` or `my-branch`.

## Commit messages

Use the Conventional Commits format:

```text
<type>(<scope>): <short description>
```

The scope is optional. Use lowercase imperative wording, do not end the subject with a period, and keep the subject concise.

Examples:

```text
feat(order): add quantity validation
fix(auth): handle expired tokens
test(order): add service unit tests
docs: update contribution guide
build: configure Java 21
ci: run Maven tests
chore: update project settings
```

Make each commit contain one logical change. Use the commit body for important implementation details or breaking changes.

## Common commit prefixes

- `feat`: add a feature
- `fix`: fix a bug
- `test`: add or update tests
- `docs`: update documentation
- `build`: change Maven or project build files
- `ci`: change GitHub Actions
- `chore`: maintenance changes
