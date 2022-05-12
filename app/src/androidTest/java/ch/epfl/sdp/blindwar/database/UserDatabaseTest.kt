package ch.epfl.sdp.blindwar.database

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindwar.game.util.GameUtil
import ch.epfl.sdp.blindwar.profile.fragments.ProfileFragment
import ch.epfl.sdp.blindwar.profile.model.User
import com.google.android.gms.tasks.Tasks.await
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class UserDatabaseTest : TestCase() {
    private val testUID = "JOJO"

    @Before
    fun init() {
        runBlocking {
            await(Firebase.auth.signInWithEmailAndPassword("test@bot.ch", "testtest"))
        }
    }

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
        launchFragmentInContainer<ProfileFragment>()
        val firstName = "David"
        val lastName = "Goodenough"
        UserDatabase.database
        runBlocking {
            var user: User?
            withContext(Dispatchers.IO) {
                user =
                    await(
                        UserDatabase.setFirstName(testUID, firstName)
                            .continueWithTask {
                                UserDatabase.setLastName(testUID, lastName)
                            }.continueWithTask {
                                UserDatabase.userReference.child(testUID).get()
                            }).getValue(User::class.java)
            }

            assertTrue(user?.firstName == firstName)
            assertTrue(user?.lastName == lastName)
        }
    }

    @Test
    fun setGenderCorrectly() {
        launchFragmentInContainer<ProfileFragment>()
        val gender = "MALE"
        runBlocking {
            var user: User?
            withContext(Dispatchers.IO) {
                user =
                    await(
                        UserDatabase.setGender(testUID, gender)
                            .continueWithTask {
                                UserDatabase.userReference.child(testUID).get()
                            }).getValue(User::class.java)
            }

            assertTrue(user?.gender == gender)
        }
    }

    @Test
    fun setPseudoCorrectly() {
        launchFragmentInContainer<ProfileFragment>()
        val pseudo = "Cirrus"
        runBlocking {
            var user: User?
            withContext(Dispatchers.IO) {
                user =
                    await(
                        UserDatabase.setPseudo(testUID, pseudo)
                            .continueWithTask {
                                UserDatabase.userReference.child(testUID).get()
                            }).getValue(User::class.java)
            }

            assertTrue(user?.pseudo == pseudo)
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
        launchFragmentInContainer<ProfileFragment>()
        val fail = 1
        val score = 2
        var user: User?
        runBlocking {
            withContext(Dispatchers.IO) {
                user = await(
                    UserDatabase.updateSoloUserStatistics(testUID, score, fail).continueWithTask {
                        UserDatabase.userReference.child(testUID).get()
                    }).getValue(User::class.java)
            }
        }

        //assertTrue(user?.userStatistics?.correctArray?.last() == score)
        //assertTrue(user?.userStatistics?.wrongArray?.last() == fail)
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
        launchFragmentInContainer<ProfileFragment>()
        var user: User?
        runBlocking {
            withContext(Dispatchers.IO) {
                user = await(UserDatabase.addLikedMusic(testUID, GameUtil.fly).continueWithTask {
                    UserDatabase.userReference.child(testUID).get()
                }).getValue(User::class.java)
            }
        }

        assertTrue(user?.likedMusics?.last()?.getName() == GameUtil.fly.getName())
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