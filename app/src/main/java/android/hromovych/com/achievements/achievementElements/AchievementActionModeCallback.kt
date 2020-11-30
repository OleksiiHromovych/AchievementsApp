package android.hromovych.com.achievements.achievementElements

import android.content.Context
import android.hromovych.com.achievements.R
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem

class AchievementActionModeCallback(
    private val context: Context?,
    private val achievementAdapter: AchievementAdapter,
    private val fragment: AchievementFragment
) : ActionMode.Callback {

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        mode?.menuInflater?.inflate(R.menu.achievement_list_action_mode, menu)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        menu?.findItem(R.id.action_delete)?.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
        menu?.findItem(R.id.action_complete)?.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
        menu?.findItem(R.id.action_activate)?.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)

        return true
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_delete -> {
                fragment.deleteRows()
            }
            R.id.action_activate -> fragment.activateRows()
            R.id.action_complete -> fragment.completeRows()
            R.id.action_select_all -> fragment.selectAllAchievements()
            R.id.action_unselect_all -> fragment.unselectAllAchievements()
            else -> throw Exception("Unknown action for action mode")
        }

        return true
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        achievementAdapter.removeSelection()
        fragment.setNullToActionMode()
    }
}