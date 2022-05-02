package ch.epfl.sdp.blindwar.database

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindwar.game.util.Tutorial
import ch.epfl.sdp.blindwar.profile.fragments.ProfileFragment
import ch.epfl.sdp.blindwar.profile.model.User
import junit.framework.TestCase
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class UserDatabaseTest : TestCase() {
    private val testUID = "JOJO"


    @Test
    fun setEloCorrectly() {
        launchFragmentInContainer<ProfileFragment>().onFragment {
            val elo = 1000
            UserDatabase.database
            UserDatabase.setElo(testUID, elo)
            UserDatabase.userReference.child(testUID).get().addOnSuccessListener {
                assertTrue((it.getValue(User::class.java)?.userStatistics?.elo == elo))
            }
        }
    }

    @Test
    fun setBirthdateCorrectly() {
        launchFragmentInContainer<ProfileFragment>().onFragment {
            val birthdate = 1000L
            UserDatabase.database
            UserDatabase.setBirthdate(testUID, birthdate)
            UserDatabase.userReference.child(testUID).get().addOnSuccessListener {
                assertTrue((it.getValue(User::class.java)?.birthDate == birthdate))
            }
        }
    }

    @Test
    fun setDescriptionCorrectly() {
        launchFragmentInContainer<ProfileFragment>().onFragment {
            val description = "User description"
            UserDatabase.database
            UserDatabase.setDescription(testUID, description)
            UserDatabase.userReference.child(testUID).get().addOnSuccessListener {
                assertTrue((it.getValue(User::class.java)?.description == description))
            }
        }
    }

    @Test
    fun setNamesCorrectly() {
        launchFragmentInContainer<ProfileFragment>().onFragment {
            val firstName = "David"; val lastName = "Goodenough"
            UserDatabase.database
            UserDatabase.setFirstName(testUID, firstName)
            UserDatabase.setLastName(testUID, lastName)
            UserDatabase.userReference.child(testUID).get().addOnSuccessListener {
                assertTrue((it.getValue(User::class.java)?.firstName == firstName))
                assertTrue((it.getValue(User::class.java)?.lastName == firstName))
            }
        }
    }

    @Test
    fun setGenderCorrectly() {
        launchFragmentInContainer<ProfileFragment>().onFragment {
            val gender = "MALE"
            UserDatabase.database
            UserDatabase.setGender(testUID, gender)
            UserDatabase.userReference.child(testUID).get().addOnSuccessListener {
                assertTrue((it.getValue(User::class.java)?.gender == gender))
            }
        }
    }

    @Test
    fun setPseudoCorrectly() {
        launchFragmentInContainer<ProfileFragment>().onFragment {
            val pseudo = "Cirrus"
            UserDatabase.database
            UserDatabase.setPseudo(testUID, pseudo)
            UserDatabase.userReference.child(testUID).get().addOnSuccessListener {
                assertTrue((it.getValue(User::class.java)?.pseudo == pseudo))
            }
        }
    }

    @Test
    fun addProfilePictureCorrectly() {
        launchFragmentInContainer<ProfileFragment>().onFragment {
            val path = "TEST_PATH"
            UserDatabase.addProfilePicture(testUID, path)
            UserDatabase.userReference.child(testUID).get().addOnSuccessListener {
                assertTrue((it.getValue(User::class.java)?.profilePicture == path))
            }
        }
    }

    @Test
    fun getUserStatisticsTest() {
        launchFragmentInContainer<ProfileFragment>().onFragment {
            UserDatabase.updateSoloUserStatistics(testUID, 1, 1)
            UserDatabase.getUserStatistics(testUID).addOnSuccessListener {
                // Assert that last game has the correct stats
                val stats = (it.getValue(User::class.java)?.userStatistics)
                assertTrue(stats?.losses?.last() == 1)
                assertTrue(stats?.wins?.last() == 1)
            }
        }
    }

    // TODO
    @Test
    fun removeUserTest() {
        launchFragmentInContainer<ProfileFragment>().onFragment {
            //UserDatabase.removeUser("")
            //assertNotNull(UserDatabase.getImageReference(testUID))
        }
    }

    @Test
    fun addLikedMusicTest() {
        launchFragmentInContainer<ProfileFragment>().onFragment {
            UserDatabase.addLikedMusic(testUID, Tutorial.fly)
            UserDatabase.userReference.child(testUID).get().addOnSuccessListener {
                assertTrue((it.getValue(User::class.java)?.likedMusics?.last()?.getName() == Tutorial.fly.getName()))
            }
        }
    }

    @Test
    fun getImageReferenceCorrectly() {
        launchFragmentInContainer<ProfileFragment>().onFragment {
            assertNotNull(UserDatabase.getImageReference(testUID))
        }
    }

    @Test
    fun getEloReferenceCorrectly() {
        launchFragmentInContainer<ProfileFragment>().onFragment {
            assertNotNull(UserDatabase.getEloReference(testUID))
        }
    }
}