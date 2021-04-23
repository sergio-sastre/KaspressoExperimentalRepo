package sergio.sastre.experimental.roboelectricvskaspresso.firstscreen

import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.kaspersky.components.kautomator.component.edit.UiEditText
import com.kaspersky.components.kautomator.component.text.UiButton
import com.kaspersky.components.kautomator.screen.UiScreen

object FirstScreen : UiScreen<FirstScreen>(),
    FirstScreenInteraction {

    override val packageName: String = getInstrumentation().targetContext.packageName

    val firstNameEditText = UiEditText { withId(this@FirstScreen.packageName, "firstName") }
    val lastNameEditText = UiEditText { withId(this@FirstScreen.packageName, "lastName") }
    val ageEditText = UiEditText { withId(this@FirstScreen.packageName, "age") }

    val nextButton = UiButton { withId(this@FirstScreen.packageName, "buttonNext") }
    val maleButton = UiButton { withId(this@FirstScreen.packageName, "male") }

    override fun writeFirstName(firstName: String) {
        firstNameEditText.replaceText(firstName)
    }

    override fun writeLastName(lastName: String) {
        lastNameEditText.replaceText(lastName)
    }

    override fun writeAge(age: String) {
        ageEditText.replaceText(age)
    }

    override fun clickOnMale() {
        maleButton.click()
    }

    override fun clickOnNext() {
        nextButton.click()
    }
}