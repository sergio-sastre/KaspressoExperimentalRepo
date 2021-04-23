package sergio.sastre.experimental.roboelectricvskaspresso.secondscreen

import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.kaspersky.components.kautomator.component.edit.UiEditText
import com.kaspersky.components.kautomator.component.text.UiButton
import com.kaspersky.components.kautomator.screen.UiScreen

object SecondScreen : UiScreen<SecondScreen>(),
    SecondScreenInteraction {

    override val packageName: String = getInstrumentation().targetContext.packageName

    val telephoneEditText = UiEditText { withId(this@SecondScreen.packageName, "telephoneNumber") }
    val faxEditText = UiEditText { withId(this@SecondScreen.packageName, "fax") }
    val emailEditText = UiEditText { withId(this@SecondScreen.packageName, "email") }

    val nextButton = UiButton { withId(this@SecondScreen.packageName, "nextButton") }

    override fun writeTelephoneNumber(telephone: String) {
        telephoneEditText.replaceText(telephone)
    }

    override fun writeFax(fax: String) {
        faxEditText.replaceText(fax)
    }

    override fun writeEmail(email: String) {
        emailEditText.replaceText(email)
    }

    override fun clickNextButton() {
        nextButton.click()
    }
}