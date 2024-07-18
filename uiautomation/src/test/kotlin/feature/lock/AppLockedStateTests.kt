package feature.lock

import helper.TestBase
import navigator.OnboardingNavigator
import navigator.screen.OnboardingScreen
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junitpioneer.jupiter.RetryingTest
import screen.dashboard.DashboardScreen
import screen.menu.MenuScreen
import screen.security.PinScreen

@DisplayName("${AppLockedStateTests.USE_CASE} App locked state [${AppLockedStateTests.JIRA_ID}]")
class AppLockedStateTests : TestBase() {

    companion object {
        const val USE_CASE = "UC 2.3"
        const val JIRA_ID = "PVW-868"
    }

    private lateinit var pinScreen: PinScreen

    @BeforeEach
    fun setUp() {
        OnboardingNavigator().toScreen(OnboardingScreen.Dashboard)

        DashboardScreen().clickMenuButton()
        MenuScreen().clickLogoutButton()

        pinScreen = PinScreen()
    }

    @RetryingTest(value = MAX_RETRY_COUNT, name = "{displayName} - {index}")
    @DisplayName("$USE_CASE.1 When the app boots it is locked and displays the PIN entry screen. [${JIRA_ID}]")
    fun verifyAppLocked() {
        assertTrue(pinScreen.pinScreenVisible(), "pin screen is not visible")
    }
}
