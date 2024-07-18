# Wallet App (Flutter)

This README explains how to configure the wallet app. It also contains the currently supported mock scenarios, describes our conventions and project structure and finally offers information on the design decisions we made in the Architecture section.

# Table of contents

- [Running the App](#running-the-app)
    * [Environment Setup](#environment-setup)
    * [App Configuration](#app-configuration)
- [File structure](#file-structure)
    * [Assets](#assets)
    * [Localization](#localization)
    * [Deeplink Scenarios](#deeplink-scenarios)
- [Conventions](#conventions)
    * [Dart](#dart)
    * [Naming](#naming)
    * [Testing](#testing)
- [Architecture](#architecture)
    * [UI Layer](#ui-layer)
    * [Domain Layer](#domain-layer)
    * [Data Layer](#data-layer)
    * [Motivation](#motivation)
- [App Distribution](#app-distribution)


# Running the App

## Environment Setup

To run the app you need to configure Flutter, Rust, the Android SDK and the iOS SDK. See the project root's [README.md](../README.md) for instructions on how to do so.

## Running

After setting up your environment, launch an Android emulator or the iOS simulator and execute:
- `flutter run` or
- `fvm flutter run` when using Flutter Version Manager (FVM)

Note that when using FVM, all Flutter commands below should be prefixed with `fvm`.

## Building

The easiest way to build the app locally is to use [fastlane](https://docs.fastlane.tools/).
To install use: `bundle install`.

### iOS

To build the iOS app use: `bundle exec fastlane ios build`. Note that password for fastlane match repository will be requested while building,
this repository is not public. Alternatively you can build the app using `flutter build ios`, which will rely on your own certificate.

### Android
To build the Android app use: `bundle exec fastlane android build`. Note that this requires you to add a local signing key to the project:
1. Get the `nl-wallet-android-local-signing-key` secrets from the secrets repository.
2. Move `local-keystore.jks` file into the `wallet_app/android/keystore` folder.
3. Move the `key.properties` file into the `wallet_app/android` folder.
4. That's it! Building release builds, e.g. with `bundle exec fastlane android build` should now work.

## App Configuration

We are currently maintaining two 'flavours' of the app, a *mock* and an *implementation* version.
The mocked version is what is used to demonstrate and verify potential use cases of the app. This version is also used for preliminary usability research and contains many mock (fake) scenarios that can be triggered using the QR codes or Deeplinks provided below.

The 'implementation' version is meant to grow into a real, functioning MVP. When running this version of the app you will quickly run into an error screen. As the work on the implementation progresses we aim to add more and more functionality (e.g. setting up your account, connecting with digid, communicating to a PID provider to retrieve a PID) and thereby 'push back' this error screen. The end goal is of course to eliminate this error screen completely, as all functionality of the mocked app will be implemented.

By default the app is launched in mock mode; to configure this manually you can set the `MOCK_REPOSITORIES` variable.
As an example, to run the app in 'implementation' mode use: `flutter run --dart-define=MOCK_REPOSITORIES=false`

During local development, two additional variables should be set:

* Setting `ENV_CONFIGURATION=true` will allow overriding of hardcoded configuration values in the Rust core from the environment during compilation.


# File Structure

## Assets

All files used by the project (like images, fonts, etc.), go into the `assets/` directory and their appropriate sub-directories.

Note; the `assets/non-free/images/` directory contains [resolution-aware images](https://flutter.dev/docs/development/ui/assets-and-images#resolution-aware).

> Copyright note: place all non free (copyrighted) assets used under the `non-free/` directory inside the appropriate asset sub-directory.

## Localization

Text localization is enabled; currently supporting English & Dutch, with English set as primary (fallback) language. Localized messages are generated based on `ARB` files found in the `lib/l10n` directory.

To support additional languages, please visit the tutorial on [Internationalizing Flutter apps](https://flutter.dev/docs/development/accessibility-and-localization/internationalization).

Internally, this project uses the commercial Lokalise service to manage translations. This service
is currently not accessible for external contributors. For contributors with access, please
see [documentation/lokalise.md](./documentation/lokalise.md) for documentation.

## Deeplink Scenarios

Below you can find the deeplinks that can be used to trigger the supported mock scenarios.
On Android, the scenarios can be triggered from the command line by using `adb shell am start -a android.intent.action.VIEW -d "{deeplink}"`.
On iOS, the scenarios are triggered with the command `xcrun simctl openurl booted '{deeplink}'`.
Note that the deeplinks only work on debug builds. For (mock) production builds you can generate a QR code from the content and scan these using the app.

### Issuance Scenarios

| Scenario                 | Content                                         | Deeplink                                                                                                    |
|--------------------------|-------------------------------------------------|-------------------------------------------------------------------------------------------------------------|
| Driving License          | {"id":"DRIVING_LICENSE","type":"issue"}         | walletdebuginteraction://deeplink#%7B%22id%22%3A%22DRIVING_LICENSE%22%2C%22type%22%3A%22issue%22%7D         |
| Extended Driving License | {"id":"DRIVING_LICENSE_RENEWED","type":"issue"} | walletdebuginteraction://deeplink#%7B%22id%22%3A%22DRIVING_LICENSE_RENEWED%22%2C%22type%22%3A%22issue%22%7D |
| Diploma                  | {"id":"DIPLOMA_1","type":"issue"}               | walletdebuginteraction://deeplink#%7B%22id%22%3A%22DIPLOMA_1%22%2C%22type%22%3A%22issue%22%7D               |
| Health Insurance         | {"id":"HEALTH_INSURANCE","type":"issue"}        | walletdebuginteraction://deeplink#%7B%22id%22%3A%22HEALTH_INSURANCE%22%2C%22type%22%3A%22issue%22%7D        |
| VOG                      | {"id":"VOG","type":"issue"}                     | walletdebuginteraction://deeplink#%7B%22id%22%3A%22VOG%22%2C%22type%22%3A%22issue%22%7D                     |
| Multiple Diplomas        | {"id":"MULTI_DIPLOMA","type":"issue"}           | walletdebuginteraction://deeplink#%7B%22id%22%3A%22MULTI_DIPLOMA%22%2C%22type%22%3A%22issue%22%7D           |

### Disclosure Scenarios

| Scenario                  | Content                                           | Deeplink                                                                                                      |
|---------------------------|---------------------------------------------------|---------------------------------------------------------------------------------------------------------------|
| Job Application           | {"id":"JOB_APPLICATION","type":"verify"}          | walletdebuginteraction://deeplink#%7B%22id%22%3A%22JOB_APPLICATION%22%2C%22type%22%3A%22verify%22%7D          |
| Bar                       | {"id":"BAR","type":"verify"}                      | walletdebuginteraction://deeplink#%7B%22id%22%3A%22BAR%22%2C%22type%22%3A%22verify%22%7D                      |
| Marketplace Login         | {"id":"MARKETPLACE_LOGIN","type":"verify"}        | walletdebuginteraction://deeplink#%7B%22id%22%3A%22MARKETPLACE_LOGIN%22%2C%22type%22%3A%22verify%22%7D        |
| Car Rental                | {"id":"CAR_RENTAL","type":"verify"}               | walletdebuginteraction://deeplink#%7B%22id%22%3A%22CAR_RENTAL%22%2C%22type%22%3A%22verify%22%7D               |
| First Aid                 | {"id":"FIRST_AID","type":"verify"}                | walletdebuginteraction://deeplink#%7B%22id%22%3A%22FIRST_AID%22%2C%22type%22%3A%22verify%22%7D                |
| Parking Permit            | {"id":"PARKING_PERMIT","type":"verify"}           | walletdebuginteraction://deeplink#%7B%22id%22%3A%22PARKING_PERMIT%22%2C%22type%22%3A%22verify%22%7D           |
| Open Bank Account         | {"id":"OPEN_BANK_ACCOUNT","type":"verify"}        | walletdebuginteraction://deeplink#%7B%22id%22%3A%22OPEN_BANK_ACCOUNT%22%2C%22type%22%3A%22verify%22%7D        |
| Provide Contract Details  | {"id":"PROVIDE_CONTRACT_DETAILS","type":"verify"} | walletdebuginteraction://deeplink#%7B%22id%22%3A%22PROVIDE_CONTRACT_DETAILS%22%2C%22type%22%3A%22verify%22%7D |
| Create MonkeyBike Account | {"id":"CREATE_MB_ACCOUNT","type":"verify"}        | walletdebuginteraction://deeplink#%7B%22id%22%3A%22CREATE_MB_ACCOUNT%22%2C%22type%22%3A%22verify%22%7D        |
| Pharmacy                  | {"id":"PHARMACY","type":"verify"}                 | walletdebuginteraction://deeplink#%7B%22id%22%3A%22PHARMACY%22%2C%22type%22%3A%22verify%22%7D                 |
| Amsterdam Login           | {"id":"AMSTERDAM_LOGIN","type":"verify"}          | walletdebuginteraction://deeplink#%7B%22id%22%3A%22AMSTERDAM_LOGIN%22%2C%22type%22%3A%22verify%22%7D          |

### Sign Scenarios

| Scenario         | Content                                 | Deeplink                                                                                            |
|------------------|-----------------------------------------|-----------------------------------------------------------------------------------------------------|
| Rental Agreement | {"id":"RENTAL_AGREEMENT","type":"sign"} | walletdebuginteraction://deeplink#%7B%22id%22%3A%22RENTAL_AGREEMENT%22%2C%22type%22%3A%22sign%22%7D |

### E2E Test Scenarios

| Scenario             | Deep dive link                         | Explanation                                                                                                           |
|----------------------|----------------------------------------|-----------------------------------------------------------------------------------------------------------------------|
| Skip (setup) to home | walletdebuginteraction://deepdive#home | Use on clean app startup; to setup wallet with mock data and jump straight to the home (a.k.a. cards overview) screen |


# Conventions

## Dart

* Max. line length is set to 120 (Dart defaults to 80)
* Relative imports for all project files below `src` folder; for
  example: `import 'bloc/wallet_bloc.dart';`
* Trailing commas are added by default; unless it compromises readability

## Naming

* Folder naming is `singular` for folders below `src`; for example: `src/feature/wallet/widget/...`

## Testing

### Unit tests

* Test file name follows the convention: `{class_name}_test.dart`
* Test description follows the convention: `should {do something} when {some condition}`
* Tests are grouped* by the method they are testing

** Grouping tests by method is not required, but recommended when testing a specific method.

### UI / Golden tests

* UI Tests are part of the normal test files
* UI Tests are grouped in `Golden Tests`

Even though they run headless, UI tests are slower to run. The main goal of these tests are to:
- Verify correct accessibility behaviour on different configurations (orientation/display scaling/font scaling/theming)
- Detect unexpected UI changes

As such we aim to keep the UI tests minimal, focusing on testing the most important states for a
screen. This can be done by providing a mocked bloc with the state manually configured in the test.

Note that the UI renders slightly differ per platform, causing small diffs (and failing tests) when
verifying on a different host platform (e.g. mac vs linux). To circumvent this issue, we opted to
only run UI tests on mac hosts for now. Because of this it is vital to only generate
new goldens on a mac host. This can be done with `flutter test --update-goldens --tags=golden <optional_path_to_single_test_file>`.

- To only verify goldens use `flutter test --tags=golden`
- To only verify other tests use `flutter test --exclude-tags=golden`

#### Widget Test Template

To be as consistent as possible when it comes to testing widget we provide the following template. This can be used as a starting point when writing widget tests:

```dart
import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:golden_toolkit/golden_toolkit.dart';

void main() {

  group('goldens', () {
    testGoldens(
      'light text',
      (tester) async {
        await tester.pumpWidgetWithAppWrapper(
          Text('T'),
        );
        await screenMatchesGolden(tester, 'text/light');
      },
    );
    testGoldens(
      'dark text',
      (tester) async {
        await tester.pumpWidgetWithAppWrapper(
          Text('T'),
          brightness: Brightness.dark,
        );
        await screenMatchesGolden(tester, 'text/dark');
      },
    );
  });

  group('widgets', () {
    testWidgets('widget is visible', (tester) async {
      await tester.pumpWidgetWithAppWrapper(
        Text('T'),
      );

      // Validate that the widget exists
      final widgetFinder = find.text('T');
      expect(widgetFinder, findsOneWidget);
    });
  });
}
```


# Architecture

> Note: This section needs to be updated, as some changes have since be made. Mainly splitting the mock and real implementation at the Repository layer in favour of the DataSource layer.

The project uses the [flutter_bloc](https://pub.dev/packages/flutter_bloc) package to handle state
management.

On top of that it follows the [BLoC Architecture](https://bloclibrary.dev/#/architecture) guide,
with a slightly fleshed out domain layer.

This architecture relies on three conceptual layers, namely:

- [UI Layer](#ui-layer)
- [Domain Layer](#domain-layer)
- [Data Layer](#data-layer)

![Architecture Overview](./architecture_overview.png)

## UI Layer

Responsible for displaying the application data on screen.

In the above diagram the **UI** node likely represents a `Widget`, e.g. in the form of a `xyzScreen`
, that observes the Bloc using one of the `flutter_bloc` provided Widgets. E.g. `BlocBuilder`.

When the user interacts with the UI, the UI is responsible for sending a corresponding `Event` to
the associated BLoC. The BLoC than processes this event and emits an updated `State` to the UI,
causing the UI to rebuild and render the new state.

## Domain Layer

Encapsulate business logic to make it reusable. UseCases are likely to be used by BLoCs to interact
with data, allowing the BLoCs to be concise and keep their focus on converting events into new
states.

Naming convention `verb in present tense + noun/what (optional) + UseCase` e.g. LogoutUserUseCase.

## Data Layer

Exposes application data to the rest of the application. Responsible for any CRUD like operations or
network interactions, likely through other classes in the form of DataSources.

## Motivation

The reason we opted for this BLoC layered approach with the intermediary domain layer is to optimize
for: Re-usability, Testability and Readability.

**Re-usable** because the usecases in the domain layer are focused on a single task, making them
convenient to re-use in multiple blocs when the same data or interaction is required on multiple (
ui) screens.

**Testable** because with the abstraction to other layers in the form of dependencies of a class,
the dependencies can be easily swapped out by mock implementations, allowing us to create small,
non-flaky unit tests of all individual components.

**Readable** because with there is a clear separation of concerns between the layers, the UI is
driven by data models (not by state living in UI components) and can thus be easily inspected, there
is a single source of truth for the data in the data layer and there is a unidirectional data flow
in the ui layer making it easier to reason about the transitions between different states.

Finally, since while we are developing the initial Proof of Concept it is unlikely that we will be
working with real datasources, this abstraction allows us to get started now, and in theory quickly
migrate to a fully functional app (once the data comes online) by replacing our MockRepositories /
MockDataSources with the actual implementations, without touching anything in the Domain or UI
Layer.


# App Distribution

## TestFlight iOS app distribution

Currently it's work in progress to setup a MacOS host machine to enable Continuous Delivery for the iOS app. Therefore; follow the steps below in order to deliver a test version of the app to iOS users via TestFlight.

### Prerequisites

* Apple ID with access to App Store Connect
* App-specific password
* Fastlane Match Passphrase

*Credentials and access are available within the team (ask around).*

### Setup prerequisites (1 time action)

* Login to appleid.com
* Create an App-specific password
* Store the created App-specific password & fastlane username/password as environment variables:
```
export FASTLANE_USER="{AppleID email address}"
export FASTLANE_PASSWORD="{Fastlane Match Passphrase}"
export FASTLANE_APPLE_APPLICATION_SPECIFIC_PASSWORD="{App-specific password}"
```

### Build & upload IPA

* Run `bundle install` from the project root folder
* Run `bundle exec fastlane match appstore --readonly`  to locally install App Store certificate & provisioning profile (password protected: "Fastlane Match Passphrase")
* Check latest iOS build number here: [App Store Connect - iOS Builds](https://appstoreconnect.apple.com/apps/SSSS/testflight/ios), next build number needs to be `{latest_build_numer} + 1`
* Build app with updated build number `UL_HOSTNAME=app.example.com bundle exec fastlane ios build app_store:true build:{next_build_number} bundle_id:nl.ictu.edi.wallet.latest app_name:"NL Wallet (latest)" universal_link_base:app.example.com`
* Upload to TestFlight `bundle exec fastlane ios deploy bundle_id:nl.ictu.edi.wallet.latest`  (login with Apple ID + password; app specific password!)
