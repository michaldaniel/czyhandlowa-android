package eu.morningbird.czyhandlowa

/*
 *  This file is part of "Czy handlowa?" android application.
 *
 *  "Czy handlowa?" is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  "Czy handlowa?" is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with "Czy handlowa?". If not, see <http://www.gnu.org/licenses/>.
*/

import eu.morningbird.czyhandlowa.util.ShoppingSunday
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*


class ShoppingSundayTest {

    private fun getAllSundaysInAYear(sunday: GregorianCalendar): List<GregorianCalendar> {
        if (sunday.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            throw IllegalArgumentException("Is not Sunday")
        }
        val sundays = ArrayList<GregorianCalendar>()
        val year = sunday.get(Calendar.YEAR)
        while (sunday.get(Calendar.YEAR) == year) {
            sundays.add(
                GregorianCalendar(
                    sunday.get(Calendar.YEAR),
                    sunday.get(Calendar.MONTH),
                    sunday.get(Calendar.DATE)
                )
            )
            sunday.add(Calendar.DATE, 7)
        }
        return sundays
    }


    @Test
    fun allSundaysInYear2018_AmountOfOpenSundaysMatches_True() {
        var openSundays = 0
        for (sunday in getAllSundaysInAYear(GregorianCalendar(2018, 0, 7))) {
            if (ShoppingSunday.areShopsOpen(sunday)) openSundays++
        }
        assertEquals(29, openSundays)
    }

    @Test
    fun allSundaysInYear2019_AmountOfOpenSundaysMatches_True() {
        var openSundays = 0
        for (sunday in getAllSundaysInAYear(GregorianCalendar(2019, 0, 6))) {
            if (ShoppingSunday.areShopsOpen(sunday)) openSundays++
        }
        assertEquals(15, openSundays)
    }

    @Test
    fun allSundaysInYear2020_AmountOfOpenSundaysMatches_True() {
        var openSundays = 0
        for (sunday in getAllSundaysInAYear(GregorianCalendar(2020, 0, 5))) {
            if (ShoppingSunday.areShopsOpen(sunday)) openSundays++
        }
        assertEquals(7, openSundays)
    }

    @Test
    fun previousYearsBeforeBan_AreShopsOpen_Open() {
        assertEquals(true, ShoppingSunday.areShopsOpen(GregorianCalendar(2016, 1, 11)))
        assertEquals(true, ShoppingSunday.areShopsOpen(GregorianCalendar(2015, 1, 18)))
        assertEquals(true, ShoppingSunday.areShopsOpen(GregorianCalendar(2013, 1, 24)))
    }

    @Test
    fun year2018BeforeBanSundays_AreShopsOpen_Open() {
        assertEquals(true, ShoppingSunday.areShopsOpen(GregorianCalendar(2018, 1, 11)))
        assertEquals(true, ShoppingSunday.areShopsOpen(GregorianCalendar(2018, 1, 18)))
        assertEquals(true, ShoppingSunday.areShopsOpen(GregorianCalendar(2018, 1, 24)))
    }

    @Test
    fun year2018BasicSundays_AreShopsOpen_Open() {
        assertEquals(true, ShoppingSunday.areShopsOpen(GregorianCalendar(2018, 9, 7)))
        assertEquals(true, ShoppingSunday.areShopsOpen(GregorianCalendar(2018, 9, 28)))
        assertEquals(true, ShoppingSunday.areShopsOpen(GregorianCalendar(2018, 10, 4)))
        assertEquals(true, ShoppingSunday.areShopsOpen(GregorianCalendar(2018, 10, 25)))
    }

    @Test
    fun year2019BasicSundays_AreShopsOpen_Open() {
        assertEquals(true, ShoppingSunday.areShopsOpen(GregorianCalendar(2019, 0, 27)))
        assertEquals(true, ShoppingSunday.areShopsOpen(GregorianCalendar(2019, 1, 24)))
        assertEquals(true, ShoppingSunday.areShopsOpen(GregorianCalendar(2019, 3, 28)))
        assertEquals(true, ShoppingSunday.areShopsOpen(GregorianCalendar(2019, 11, 29)))
    }

    @Test
    fun year2019BeforeChristmasSundays_AreShopsOpen_Open() {
        assertEquals(true, ShoppingSunday.areShopsOpen(GregorianCalendar(2019, 11, 15)))
        assertEquals(true, ShoppingSunday.areShopsOpen(GregorianCalendar(2019, 11, 22)))
    }

    @Test
    fun year2019BeforeEasterSunday_AreShopsOpen_Open() {
        assertEquals(true, ShoppingSunday.areShopsOpen(GregorianCalendar(2019, 3, 14)))
    }

    @Test
    fun year2020BasicSundays_AreShopsOpen_Open() {
        assertEquals(true, ShoppingSunday.areShopsOpen(GregorianCalendar(2020, 0, 26)))
        assertEquals(true, ShoppingSunday.areShopsOpen(GregorianCalendar(2020, 3, 26)))
        assertEquals(true, ShoppingSunday.areShopsOpen(GregorianCalendar(2020, 5, 28)))
        assertEquals(true, ShoppingSunday.areShopsOpen(GregorianCalendar(2020, 7, 30)))
    }

    @Test
    fun year2020BeforeChristmasSundays_AreShopsOpen_Open() {
        assertEquals(true, ShoppingSunday.areShopsOpen(GregorianCalendar(2020, 11, 13)))
        assertEquals(true, ShoppingSunday.areShopsOpen(GregorianCalendar(2020, 11, 20)))

    }

    @Test
    fun year2020BeforeEasterSunday_AreShopsOpen_Open() {
        assertEquals(true, ShoppingSunday.areShopsOpen(GregorianCalendar(2020, 3, 5)))
    }


    @Test
    fun forEveryDayOfWeek_isNextSundayCorrect_True() {
        assertEquals(false, ShoppingSunday.areShopsOpen(GregorianCalendar(2019, 1, 17)))
        assertEquals(true, ShoppingSunday.areShopsOpen(GregorianCalendar(2019, 1, 18)))
        assertEquals(true, ShoppingSunday.areShopsOpen(GregorianCalendar(2019, 1, 19)))
        assertEquals(true, ShoppingSunday.areShopsOpen(GregorianCalendar(2019, 1, 20)))
        assertEquals(true, ShoppingSunday.areShopsOpen(GregorianCalendar(2019, 1, 21)))
        assertEquals(true, ShoppingSunday.areShopsOpen(GregorianCalendar(2019, 1, 22)))
        assertEquals(true, ShoppingSunday.areShopsOpen(GregorianCalendar(2019, 1, 23)))
        assertEquals(true, ShoppingSunday.areShopsOpen(GregorianCalendar(2019, 1, 24)))
        assertEquals(false, ShoppingSunday.areShopsOpen(GregorianCalendar(2019, 1, 25)))
    }


    @Test
    fun year2018BasicSundays_AreShopsOpen_Closed() {
        assertEquals(false, ShoppingSunday.areShopsOpen(GregorianCalendar(2018, 2, 11)))
        assertEquals(false, ShoppingSunday.areShopsOpen(GregorianCalendar(2018, 2, 18)))
        assertEquals(false, ShoppingSunday.areShopsOpen(GregorianCalendar(2018, 10, 11)))
        assertEquals(false, ShoppingSunday.areShopsOpen(GregorianCalendar(2018, 10, 18)))
    }

    @Test
    fun year2019BasicSundays_AreShopsOpen_Closed() {
        assertEquals(false, ShoppingSunday.areShopsOpen(GregorianCalendar(2019, 0, 6)))
        assertEquals(false, ShoppingSunday.areShopsOpen(GregorianCalendar(2019, 0, 13)))
        assertEquals(false, ShoppingSunday.areShopsOpen(GregorianCalendar(2019, 0, 20)))
        assertEquals(false, ShoppingSunday.areShopsOpen(GregorianCalendar(2019, 1, 17)))
    }

    @Test
    fun year2020BasicSundays_AreShopsOpen_Closed() {
        assertEquals(false, ShoppingSunday.areShopsOpen(GregorianCalendar(2020, 1, 2)))
        assertEquals(false, ShoppingSunday.areShopsOpen(GregorianCalendar(2020, 1, 9)))
        assertEquals(false, ShoppingSunday.areShopsOpen(GregorianCalendar(2020, 1, 16)))
        assertEquals(false, ShoppingSunday.areShopsOpen(GregorianCalendar(2020, 1, 23)))
    }

    @Test
    fun year2020LastSundeys_AreShopsOpen_Closed() {
        assertEquals(false, ShoppingSunday.areShopsOpen(GregorianCalendar(2020, 1, 23)))
        assertEquals(false, ShoppingSunday.areShopsOpen(GregorianCalendar(2020, 2, 29)))
        assertEquals(false, ShoppingSunday.areShopsOpen(GregorianCalendar(2020, 4, 31)))
        assertEquals(false, ShoppingSunday.areShopsOpen(GregorianCalendar(2020, 6, 26)))
        assertEquals(false, ShoppingSunday.areShopsOpen(GregorianCalendar(2020, 8, 27)))
        assertEquals(false, ShoppingSunday.areShopsOpen(GregorianCalendar(2020, 9, 25)))
        assertEquals(false, ShoppingSunday.areShopsOpen(GregorianCalendar(2020, 10, 29)))
        assertEquals(false, ShoppingSunday.areShopsOpen(GregorianCalendar(2020, 11, 27)))
    }
}