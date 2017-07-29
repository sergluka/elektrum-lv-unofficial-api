package command.base

import com.github.rvesse.airline.types.DefaultTypeConverter
import java.time.LocalDate
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField

class CustomTypeConverter : DefaultTypeConverter() {

    private val formatter = DateTimeFormatterBuilder()
            .appendPattern("yyyy")
            .optionalStart()
                .appendLiteral("-")
                .appendPattern("MM")
                .optionalStart()
                    .appendLiteral("-")
                    .appendPattern("dd")
                .optionalEnd()
            .optionalEnd()
            .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
            .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
            .toFormatter()


    override fun convert(name: String, type: Class<*>, value: String): Any {
        checkArguments(name, type, value)

        if (LocalDate::class.java.isAssignableFrom(type)) {
            return LocalDate.parse(value, formatter)
        }

        return super.convert(name, type, value)
    }
}
