package android.hromovych.com.achievements.achievementElements

import android.content.Context
import android.hromovych.com.achievements.R
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast

class AchievementActionModeCallback(
    private val context: Context?,
    private val achievementAdapter: AchievementAdapter,
    private val achievements: List<Achievement>,
    private val fragment: AchievementFragment
) : ActionMode.Callback {

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        mode?.menuInflater?.inflate(R.menu.achievement_list_action_mode, menu)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        menu?.findItem(R.id.action_delete)?.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        menu?.findItem(R.id.action_copy)?.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)

        return true
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_delete -> {
                Toast.makeText(context, "delete from actionMode", Toast.LENGTH_SHORT).show()
            }
        }

        return true
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        achievementAdapter.removeSelection()
        fragment.setNullToActionMode()
    }
}