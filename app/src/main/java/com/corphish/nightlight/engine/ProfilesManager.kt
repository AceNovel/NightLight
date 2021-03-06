package com.corphish.nightlight.engine

import android.content.Context
import android.preference.PreferenceManager
import com.corphish.nightlight.extensions.toArrayOfInts
import java.util.*

class ProfilesManager(private val context: Context) {

    private val _prefKey = "pref_profiles_store"

    /**
     * Listens to changes in data
     */
    interface DataChangeListener {
        /**
         * Callback when data change occurs
         * @param newDataSize Size of new data
         */
        fun onDataChanged(newDataSize: Int)
    }

    private var dataChangeListener: DataChangeListener? = null

    fun registerDataChangeListener(dataChangeListener: DataChangeListener) {
        this.dataChangeListener = dataChangeListener
        dataChangeListener.onDataChanged(profilesList.size)
    }

    // Profiles set maybe null (when set is empty) but will not contain null values
    val profilesList: MutableList<Profile> = mutableListOf()

    fun loadProfiles() {
        profilesList.clear()

        val savedSet = PreferenceManager.getDefaultSharedPreferences(context)
                .getStringSet(_prefKey, null)

        if (savedSet == null || savedSet.isEmpty()) return

        for (entry in savedSet) {
            val profile = parseProfile(entry)
            if (profile != null) profilesList.add(profile)
        }

        dataChangeListener?.onDataChanged(profilesList.size)
    }

    private fun storeProfiles() {
        // Need string set for shared pref
        val stringSet: MutableSet<String> = hashSetOf()

        profilesList.forEach { profile -> stringSet += profile.toProfileString() }
        dataChangeListener?.onDataChanged(profilesList.size)

        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putStringSet(_prefKey, stringSet)
                .apply()
    }

    /**
     * Creates a new profile, stores it in local set and then writes it to SP
     * @param enabled Whether the setting should be enabled or not
     * @param name Name of profile
     * @param mode Setting mode of profile
     * @param settings Settings of profile
     * @return A boolean indicating whether profile creation was successful or not
     */
    fun createProfile(enabled: Boolean, name: String, mode: Int, settings: IntArray): Boolean {
        val ret = profilesList.add(Profile(name, enabled, mode, settings))

        if (ret) storeProfiles()

        return ret
    }

    /**
     * Deletes a profile with given name, and then updates the changes in SP
     * @param profile Profile to be deleted
     */
    fun deleteProfile(profile: Profile) : Boolean {
        val ret = profilesList.remove(profile)
        storeProfiles()

        return ret
    }

    /**
     * Updates an existing profile with new params
     * @param oldName Old name of profile to be update
     * @param enabled Whether setting should be enabled or not
     * @param newName New name
     * @param newMode New mode
     * @param newSettings New settings
     * @return Whether profile update was successful or not
     */
    fun updateProfile(oldProfile: Profile, enabled: Boolean, newName: String, newMode: Int, newSettings: IntArray): Boolean {
        val ret = deleteProfile(oldProfile)
        if (!ret) return false

        createProfile(enabled, newName, newMode, newSettings)

        return ret
    }

    fun getProfileByName(name: String) : Profile? {
        var profile: Profile? = null
        for (p in profilesList)
            if (p.name == name) profile = p

        return profile
    }

    // Profile Model
    data class Profile(var name: String,
                       var isSettingEnabled: Boolean = false,
                       var settingMode: Int = 0,
                       var settings: IntArray) : Comparable<Profile> {
        // This is how the profile will be stored as string in SP
        // For example -> Name - Profile, SettingMode - 1, settings - {100}
        // Will look like -> "Profile; 1; [100]"
        // DO NOT alter the sequence of data
        // In future if other fields are added, simply append at the end
        fun toProfileString(): String {
            return name + ";" + isSettingEnabled + ";" + settingMode + ";" + Arrays.toString(settings)
        }

        fun apply(context: Context) {
            Core.applyNightModeAsync(isSettingEnabled, context, settingMode, settings)
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other?.javaClass != javaClass) return false

            other as Profile

            return other.name == this.name &&
                    other.isSettingEnabled == this.isSettingEnabled &&
                    other.settingMode == settingMode &&
                    Arrays.equals(other.settings, this.settings)
        }

        override fun hashCode(): Int {
            return this.name.hashCode()
        }

        override fun compareTo(other: Profile): Int {
            return this.name.compareTo(other.name)
        }
    }

    private fun parseProfile(profileString: String): Profile? {
        val parts = profileString.split(";".toRegex())

        return when (parts.size == 4) {
            true -> Profile(parts[0], parts[1].toBoolean(), parts[2].toInt(), parts[3].toArrayOfInts(","))
            false -> null
        }
    }
}