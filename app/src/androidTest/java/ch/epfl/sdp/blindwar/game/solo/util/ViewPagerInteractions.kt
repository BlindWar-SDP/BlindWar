package ch.epfl.sdp.blindwar.game.solo.util

/** Source :
 *  https://github.com/android/views-widgets-samples/blob/master/ViewPager2/app/src/androidTest/java/androidx/viewpager2/integration/testapp/test/util/ViewInteractions.kt
 */

/*
 * Copyright 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.viewpager2.widget.ViewPager2
import org.hamcrest.CoreMatchers.allOf

/**
 * Creates a [ViewInteraction] that interacts with a [ViewPager2].
 */
fun onViewPager(): ViewInteraction {
    return onView(isAssignableFrom(ViewPager2::class.java))
}

/**
 * Creates a [ViewInteraction] that interacts with the currently visible page of a [ViewPager2]. The
 * currently visible page is the page that is displaying at least 50% of its content. When two pages
 * both show exactly 50%, the selected page is undefined.
 */
fun onCurrentPage(): ViewInteraction {
    return onView(
        allOf(
            withParent(withParent(isAssignableFrom(ViewPager2::class.java))),
            isDisplayingAtLeast(50)
        )
    )
}