package org.chicha.cofi.share.model

import org.json.JSONObject

interface SharedData {
    fun serialize(): JSONObject
}
