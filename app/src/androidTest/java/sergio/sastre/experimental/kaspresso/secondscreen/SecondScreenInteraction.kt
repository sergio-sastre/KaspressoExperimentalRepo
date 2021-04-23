package sergio.sastre.experimental.roboelectricvskaspresso.secondscreen

interface SecondScreenInteraction {

    fun writeTelephoneNumber(telephone: String)

    fun writeFax(fax: String)

    fun writeEmail(email: String)

    fun clickNextButton()
}