package sergio.sastre.experimental.roboelectricvskaspresso.scrollingscreen

import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.kaspersky.components.kautomator.component.text.UiButton
import com.kaspersky.components.kautomator.screen.UiScreen

object ScrollingScreen : UiScreen<ScrollingScreen>(),
    ScrollingScreenInteraction {

    override val packageName: String = getInstrumentation().targetContext.packageName

    val findMeButton = UiButton { withText("FIND ME") }

    override fun clickOnFindMeButton() {
        findMeButton.click()
    }
}