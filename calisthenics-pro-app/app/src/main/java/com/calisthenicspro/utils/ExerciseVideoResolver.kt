package com.calisthenicspro.utils

import android.content.Context
import android.net.Uri
import com.calisthenicspro.R
import java.lang.reflect.Field
import java.util.Locale

object ExerciseVideoResolver {

    private val localAliases = mapOf(
        "push_up" to "bodyweight_fitness_pushup",
        "pushup" to "bodyweight_fitness_pushup",
        "pull_up" to "bodyweight_fitness_pullup",
        "pullup" to "bodyweight_fitness_pullup",
        "squat" to "bodyweight_fitness_squat",
        "plank" to "bodyweight_fitness_plank",
        "burpee" to "bodyweight_fitness_burpees",
        "lunge" to "bodyweight_fitness_deep_step_up",
        "glute_bridge" to "bodyweight_fitness_arch",
        "dip" to "bodyweight_fitness_parallel_bar_dips",
        "dips" to "bodyweight_fitness_parallel_bar_dips",
        "reverse_lunge" to "bodyweight_fitness_deep_step_up",
        "bear_crawl" to "bodyweight_fitness_plank",
        "mountain_climber" to "bodyweight_fitness_plank",
        "dead_bug" to "bodyweight_fitness_arch",
        "glute-bridge" to "bodyweight_fitness_arch"
    )

    private val remoteFallbacks = mapOf(
        "push_up" to "https://raw.githubusercontent.com/BodyweightFitness/BodyweightFitness/master/videos/pushup.mp4",
        "pushup" to "https://raw.githubusercontent.com/BodyweightFitness/BodyweightFitness/master/videos/pushup.mp4",
        "pull_up" to "https://raw.githubusercontent.com/BodyweightFitness/BodyweightFitness/master/videos/pullup.mp4",
        "pullup" to "https://raw.githubusercontent.com/BodyweightFitness/BodyweightFitness/master/videos/pullup.mp4",
        "squat" to "https://raw.githubusercontent.com/BodyweightFitness/BodyweightFitness/master/videos/squat.mp4",
        "plank" to "https://raw.githubusercontent.com/BodyweightFitness/BodyweightFitness/master/videos/plank.mp4",
        "burpee" to "https://raw.githubusercontent.com/BodyweightFitness/BodyweightFitness/master/videos/burpee.mp4",
        "lunge" to "https://raw.githubusercontent.com/BodyweightFitness/BodyweightFitness/master/videos/lunge.mp4",
        "dip" to "https://raw.githubusercontent.com/BodyweightFitness/BodyweightFitness/master/videos/dips.mp4",
        "mountain_climber" to "https://raw.githubusercontent.com/BodyweightFitness/BodyweightFitness/master/videos/mountain_climber.mp4"
    )

    fun resolveUri(context: Context, exerciseName: String): Uri? {
        val normalized = normalize(exerciseName)

        val localResourceId = resolveLocalResourceId(normalized)
        if (localResourceId != null) {
            return Uri.parse("android.resource://${context.packageName}/$localResourceId")
        }

        val remoteUrl = remoteFallbacks[normalized]
            ?: localAliases[normalized]?.let { remoteFallbacks[it] }
            ?: remoteFallbacks.entries.firstOrNull { (key, _) -> normalized.contains(key) }?.value

        return remoteUrl?.let(Uri::parse)
    }

    private fun normalize(value: String): String {
        return value.lowercase(Locale.US)
            .replace("&", "and")
            .replace("-", "_")
            .replace(" ", "_")
            .replace("/", "_")
            .replace("[^a-z0-9_]".toRegex(), "")
            .trim('_')
    }

    private fun resolveLocalResourceId(normalized: String): Int? {
        val candidates = linkedSetOf(
            "bodyweight_fitness_$normalized",
            "bodyweight_fitness_${normalized}_mp4",
            "bodyweight_fitness_${normalized}_exercise",
            "bodyweight_fitness_${normalized}_movement"
        )

        val alias = localAliases[normalized]
        if (alias != null) {
            candidates += alias
        }

        for (candidate in candidates) {
            val id = findRawId(candidate)
            if (id != null) return id
        }

        return null
    }

    private fun findRawId(name: String): Int? {
        return try {
            val field: Field = R.raw::class.java.getField(name)
            field.getInt(null)
        } catch (_: Exception) {
            null
        }
    }
}
