# ESTIAM E5 M5 assessment - HelpDesk Lite

HelpDesk Lite tracks campus IT incidents such as a broken projector or a failed Wi-Fi connection. You will complete ticket validation, status and priority rules, Room repository operations, and the UI save workflow. You will also write an Espresso test that proves a ticket appears with the priority selected on the form.

## Working conditions

- This is a two-hour individual assessment. Plan 90 minutes for implementation and 30 minutes for debugging, verification and Git submission.
- Course materials and documentation are allowed. Work in your own Git fork of the provided starter repository.
- Do not edit the supplied tests or the test fixture to bypass the checks. The only test file you should modify is `TicketJourneyTest.kt`.
- Use the provided Gradle wrapper, Java 17 and Android SDK Platform 35. The README gives setup and test commands. Keep the dependencies and project configuration as supplied.

## Exercise 1 - Ticket validation (15 min, 4 points)

Edit `app/src/main/java/fr/estiam/helpdesklite/model/TicketValidation.kt`.

- Complete `isTitleValid`. A title is valid when it contains at least one non-whitespace character.
- Complete `isDescriptionValid`. After trimming leading and trailing whitespace, the description must contain at least 10 characters. Internal whitespace counts.
- Complete `create`. Return `null` when either field is invalid. Otherwise return a ticket with trimmed text, status `OPEN`, and exactly the supplied ID, priority and timestamp.

Run `TicketValidationTest` as a local JUnit test.

## Exercise 2 - Priority and status rules (20 min, 4 points)

Edit `app/src/main/java/fr/estiam/helpdesklite/model/TicketRules.kt`.

- Complete `canTransition`. Allow only `OPEN → IN_PROGRESS` and `IN_PROGRESS → RESOLVED`. Reject every other pair, including changes to the current status.
- Complete `sortByPriority`. Return the tickets in `HIGH`, `MEDIUM`, `LOW` order. Keep equal-priority tickets in their original relative order. Preserve every ticket and leave the input list unchanged.

Run `TicketRulesTest` as a local JUnit test.

## Exercise 3 - Room repository (20 min, 5 points)

Edit `app/src/main/java/fr/estiam/helpdesklite/data/local/RoomTicketRepository.kt`. Insertion, the database, the DAO, and row conversion are already supplied.

- Complete `findById`. Return the matching ticket with all its fields, or `null` for an unknown ID.
- Complete `updateStatus`. Use the supplied `canTransition` function and DAO operations. Return `false` for missing IDs, disallowed changes or an update that changes no row. Return `true` only when the requested update succeeds. Preserve the other fields and tickets.
- Complete `observeByStatus`. Return a Flow of matching tickets in the DAO’s order (newest first, then ID ascending for timestamp ties). The stream should reflect database changes and emit an empty list when nothing matches.

Run `RoomTicketRepositoryTest` on the emulator. It supplies a small transition policy for its fixture so that you can test repository mechanics independently of Exercise 2.

## Exercise 4 - Connect the form to persistence (20 min, 4 points)

Edit `app/src/main/java/fr/estiam/helpdesklite/ui/CreateTicketActivity.kt`, in the existing coroutine inside `saveTicket`. The form, priority selector, ticket creation, validation messages and exception handling are provided.

- Save the prepared ticket through the application’s repository. Close the form only after the insert succeeds so the catalogue can show the saved entry.
- Preserve the supplied error handling and the guard against repeated taps while saving. Remove the placeholder that re-enables Save in the success branch.
- The catalogue should show the entered title, description and selected priority on the same row. The supplied test uses valid input and can run while the earlier exercises are unfinished.

Run `CreateTicketUiTest` on the emulator.

## Exercise 5 - Write an Espresso test (15 min, 3 points)

Edit `app/src/androidTest/java/fr/estiam/helpdesklite/TicketJourneyTest.kt`. Remove `@Ignore` and retain the fixture rule and an executable JUnit 4 test.

- Use Espresso to open the form, enter the title **Keyboard in lab B** and a valid description, explicitly select **LOW**, and tap Save.
- Use `fixture.awaitCatalogueRows()` after tapping Save. Find the row by title and verify that the visible priority on that same row is **LOW**. Locate the row by content. Avoid sleeps, positional RecyclerView lookups and direct database seeding.
- The test should pass with working code and fail if that ticket is absent or its displayed priority changes. You can write the test before Exercise 4 is complete. Its workflow needs a working save to pass.

Run `TicketJourneyTest` on the emulator. Applicable IDs are `addTicket`, `titleInput`, `descriptionInput`, `priorityInput`, `saveTicket`, `ticketList`, `ticketRow`, `ticketTitle`, and `ticketPriority`.

## Run individual exercises

In Android Studio, open the named class and use its Run gutter icon. Exercises 1–2 are local JVM tests. Exercises 3–5 require a running emulator or connected device. In PowerShell, for example:

```powershell
.\gradlew.bat :app:testDebugUnitTest --tests 'fr.estiam.helpdesklite.TicketValidationTest'
.\gradlew.bat :app:connectedDebugAndroidTest '-Pandroid.testInstrumentationRunnerArguments.class=fr.estiam.helpdesklite.RoomTicketRepositoryTest'
```

Replace the class name with the one listed for the exercise. Run both full suites before submission. The five exercises total 20 points. Credit follows the behaviors listed above, including the quality of the test you write in Exercise 5.

## Submission checklist

- Confirm that the app assembles and launches. Try creating a ticket and advancing its status from the catalogue.
- Run both test suites. Confirm that your own Espresso test ran and check the reports listed in the README.
- Confirm that your changes are committed to the assessment branch on your own fork. Push that branch, for example with `git push -u origin HEAD` after confirming `origin` points to your fork.
- Submit the URL of the branch on your fork or the pull request URL through the channel specified in class.
