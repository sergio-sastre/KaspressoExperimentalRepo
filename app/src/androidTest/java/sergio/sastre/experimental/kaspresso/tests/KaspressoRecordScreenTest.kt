package sergio.sastre.experimental.kaspresso.tests

import androidx.test.ext.junit.rules.activityScenarioRule
import com.kaspersky.kaspresso.idlewaiting.KautomatorWaitForIdleSettings
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import sergio.sastre.experimental.kaspresso.MainActivity
import sergio.sastre.experimental.roboelectricvskaspresso.firstscreen.FirstScreen
import sergio.sastre.experimental.kaspresso.utils.testrules.RecordScreenTest
import sergio.sastre.experimental.kaspresso.utils.testrules.ScreenRecorderTestRule
import sergio.sastre.experimental.roboelectricvskaspresso.scrollingscreen.ScrollingScreen
import sergio.sastre.experimental.roboelectricvskaspresso.secondscreen.SecondScreen
import java.lang.RuntimeException

class KaspressoRecordScreenTest :
    TestCase(
        kaspressoBuilder = Kaspresso.Builder.simple {
            kautomatorWaitForIdleSettings = KautomatorWaitForIdleSettings.boost()
        }
    )
{
    @get:Rule
    val activityRule = activityScenarioRule<MainActivity>()

    private val testRecordingRule = ScreenRecorderTestRule(
        adbServer = adbServer,
        recordingsPath = "/Users/serikolya/Desktop"
    )

    @get:Rule
    val ruleChain: RuleChain = RuleChain.outerRule(testRecordingRule).around(activityRule)

    @RecordScreenTest(alsoOnSuccess = true)
    @Test
    fun fillFirstAndSecondScreen() {

        before {
            activityRule.scenario
        }.after {

        }.run {
            FirstScreen {
                writeFirstName("Sergio")
                writeLastName("Sastre Florez")
                writeAge("8")
                clickOnMale()

                clickOnNext()
            }

            SecondScreen {
                writeTelephoneNumber("+49 1515 328 9999")
                writeFax("+49 89 15328 9999")
                writeEmail("sergio.sastre@check24.de")
            }
        }
    }

    // Fails, so a recording is saved
    @RecordScreenTest
    @Test
    fun fillFirstAndSecondScreenAndThenCrashes() {

        before {
            activityRule.scenario
        }.after {

        }.run {
            FirstScreen {
                writeFirstName("Sergio")
                writeLastName("Sastre Florez")
                writeAge("8")
                clickOnMale()

                clickOnNext()
            }

            SecondScreen {
                writeTelephoneNumber("+49 1515 328 9999")
                writeFax("+49 89 15328 9999")
                writeEmail("sergio.sastre@check24.de")
                forceCrash()
            }
        }
    }

    // Succeeds, so no file created since alsoOnSuccess = false by default
    @RecordScreenTest
    @Test
    fun fillFirstAndSecondScreenAndClickFindMe() {

        before {
            activityRule.scenario
        }.after {
            //no-op
        }.run {
            FirstScreen {
                writeFirstName("Sergio")
                writeLastName("Sastre Florez")
                writeAge("8")
                clickOnMale()
                clickOnNext()
            }

            SecondScreen {
                writeTelephoneNumber("+49 1515 328 9999")
                writeFax("+49 89 15328 9999")
                writeEmail("sergio.sastre@check24.de")
                clickNextButton()
            }

            ScrollingScreen {
                clickOnFindMeButton()
            }
        }
    }

    private fun forceCrash(){
        throw RuntimeException()
    }
}