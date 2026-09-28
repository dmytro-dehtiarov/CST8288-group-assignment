# Contributing

## Getting started

Clone the repository and enter the project directory:

```bash
git clone https://github.com/USERNAME/REPOSITORY.git
cd REPOSITORY
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
