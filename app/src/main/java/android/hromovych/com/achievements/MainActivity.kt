package android.hromovych.com.achievements

import android.hromovych.com.achievements.achievementElements.Achievement
import android.hromovych.com.achievements.achievementElements.AchievementCallbacks
import android.hromovych.com.achievements.achievementElements.AchievementFragment
import android.hromovych.com.achievements.edit.AchievementEditFragment
import android.hromovych.com.achievements.group.Group
import android.hromovych.com.achievements.group.GroupCallbacks
import android.hromovych.com.achievements.group.GroupListFragment
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), GroupCallbacks, AchievementCallbacks {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().run {
            replace(R.id.container, GroupListFragment.newInstance())
            commit()
        }
    }

    override fun onGroupClick(group: Group) {
        supportFragmentManager.beginTransaction().run {
            replace(R.id.container, AchievementFragment.newInstance(group.id), null)
            addToBackStack(null)
            commit()
        }
    }

    override fun onAchievementClick(achievement: Achievement) {
        supportFragmentManager.beginTransaction().run {
            replace(R.id.container, AchievementEditFragment.newInstance(achievement.id))
            addToBackStack(null)
            commit()
        }
    }


}