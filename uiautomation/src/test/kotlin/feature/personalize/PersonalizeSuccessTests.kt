package feature.personalize

import helper.TestBase
import navigator.OnboardingNavigator
import navigator.screen.OnboardingScreen
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junitpioneer.jupiter.RetryingTest
import screen.dashboard.DashboardScreen
import screen.personalize.PersonalizeSuccessScreen

@DisplayName("${PersonalizeSuccessTests.USE_CASE} App confirms PID issuance to user [${PersonalizeSuccessTests.JIRA_ID}]")
class PersonalizeSuccessTests : TestBase() {

    companion object {
        const val USE_CASE = "UC 3.1"
        const val JIRA_ID = "PVW-1039"
    }

    private lateinit var personalizeSuccessScreen: PersonalizeSuccessScreen

    @BeforeEach
    fun setUp() {
        OnboardingNavigator().toScreen(OnboardingScreen.PersonalizeSuccess)

        personalizeSuccessScreen = PersonalizeSuccessScreen()
    }

    @RetryingTest(value = MAX_RETRY_COUNT, name = "{displayName} - {index}")
    @DisplayName("$USE_CASE.1 When PID was issued successfully, the App displays a confirmation to the User. [$JIRA_ID]")
    fun verifyPersonalizeSuccessScreen() {
        assertTrue(personalizeSuccessScreen.visible(), "personalize loading screen is not visible")
    }

    @RetryingTest(value = MAX_RETRY_COUNT, name = "{displayName} - {index}")
    @DisplayName("$USE_CASE.2 The confirmation includes a success message. [$JIRA_ID]")
    fun verifySuccessMessage() {
        assertTrue(personalizeSuccessScreen.successMessageVisible(), "success text is not visible")
    }

    @RetryingTest(value = MAX_RETRY_COUNT, name = "{displayName} - {index}")
    @DisplayName("$USE_CASE.3 The confirmation includes the issued cards (PID + Address): card, title. [$JIRA_ID]")
    fun verifyIssuedCards() {
        assertTrue(personalizeSuccessScreen.cardsVisible(), "cards not visible")
    }

    @RetryingTest(value = MAX_RETRY_COUNT, name = "{displayName} - {index}")
    @DisplayName("$USE_CASE.4 The App offers an entrance to enter the wallet which brings the User to the Dashboard. [$JIRA_ID]")
    fun verifyNavigateToDashboardButton() {
        personalizeSuccessScreen.clickNextButton()

        val dashboardScreen = DashboardScreen()
        assertTrue(dashboardScreen.visible(), "dashboard screen is not visible")
    }
}
