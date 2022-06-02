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
    private var user0 = User.Builder().setUid(testUID).build()


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
            user0.userStatistics.eloSetter(elo)
            UserDatabase.database
            UserDatabase.updateUser(user0)
            // TODO: Use Tasks.await to make sure that the assertions are called
            UserDatabase.userReference.child(testUID).get().addOnSuccessListener {
                assertTrue((it.getValue(User::class.java)?.userStatistics?.elo == elo))
            }
        }
    }

    @Test
    fun setBirthdateCorrectly() {
        launchFragmentInContainer<ProfileFragment>().onFragment {
            val birthdate = 1000L
            user0.birthdate = birthdate
            UserDatabase.database
            UserDatabase.updateUser(user0)
            // TODO: Use Tasks.await to make sure that the assertions are called
            UserDatabase.userReference.child(testUID).get().addOnSuccessListener {
                assertTrue((it.getValue(User::class.java)?.birthdate == birthdate))
            }
        }
    }

    @Test
    fun setDescriptionCorrectly() {
        launchFragmentInContainer<ProfileFragment>().onFragment {
            val description = "User description"
            user0.description = description
            UserDatabase.database
            UserDatabase.updateUser(user0)
            // TODO: Use Tasks.await to make sure that the assertions are called
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
        user0.firstName = firstName
        user0.lastName = lastName
        UserDatabase.database
        runBlocking {
            var user: User?
            withContext(Dispatchers.IO) {
                user =
                    await(
                        UserDatabase.updateUser(user0)!!
                            .continueWithTask {
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
        user0.gender = gender
        runBlocking {
            var user: User?
            withContext(Dispatchers.IO) {
                user =
                    await(
                        UserDatabase.updateUser(user0)!!
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
        user0.pseudo = pseudo
        runBlocking {
            var user: User?
            withContext(Dispatchers.IO) {
                user =
                    await(
                        UserDatabase.updateUser(user0)!!
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
        val fail = 2
        val score = 1

        var user: User?
        runBlocking {
            withContext(Dispatchers.IO) {
                user = await(
                    UserDatabase.updateSoloUserStatistics(testUID, score, fail).continueWithTask {
                        UserDatabase.userReference.child(testUID).get()
                    }).getValue(User::class.java)

                assertTrue(user?.userStatistics?.correctArray?.first() == score)
                assertTrue(user?.userStatistics?.wrongArray?.first() == fail)
            }
        }
    }

    @Test
    fun removeUserTest() {
        launchFragmentInContainer<ProfileFragment>().onFragment {
            UserDatabase.removeUser("test")
            assertNotNull(UserDatabase.getImageReference(testUID))
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

        assertTrue(user?.likedMusics?.last()?.name == GameUtil.fly.name)
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