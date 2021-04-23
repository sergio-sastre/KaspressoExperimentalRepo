package sergio.sastre.experimental.kaspresso.utils.testrules

import java.lang.annotation.Inherited
import kotlin.annotation.Retention

@Retention(AnnotationRetention.RUNTIME)
@Inherited
annotation class RecordScreenTest(val alsoOnSuccess: Boolean = false)