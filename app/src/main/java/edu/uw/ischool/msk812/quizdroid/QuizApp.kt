package edu.uw.ischool.msk812.quizdroid

import android.app.Application
import android.util.Log



class Question (val q: String, val a1: String, val a2: String, val a3: String,
                val a4: String, val correctAnswer: Int)
data class Quiz (
    val question: String,
    val a1: String,
    val a2: String,
    val a3: String,
    val a4: String,
    val correct: Int)

data class Topic (
    val title: String,
    val shortDesc: String,
    val longDesc: String,
    val questions: List<Question>
)

interface TopicRepository {
    fun getAllTopics() : List<Topic>
    fun getTopic(index: Int) : Topic
    fun getQuiz(topicNum: Int, index: Int) : Quiz
}

class MockTopicRepository : TopicRepository {
    val topics : List<Topic> = listOf(
        Topic("Math","This quiz will test how well you can solve basic math problems",
        "It will contain basic mathematical topics and equations around addition, multiplication and simple calculus.",
            listOf(
                Question("What is 2 + 2?","3","4","2","7",2),
                Question("What is 13 * 29?","377","437","378","245",1),
                Question("What is the square root of 144?","8","16","13","12",4)
            )
        ),
        Topic("Physics","This quiz will test on your knowledge of some physics facts.",
        "It will ask about some physics terms and formulas that you need to see which answer fits the question.",
            listOf(
                Question("What is the speed in combination of the direction of an object's motion?",
                    "Pressure","Watt","Velocity","Acceleration",3),
                Question("Which subatomic particle has neutral charge?","Proton","Antineutron",
                    "Electron","Neutron",4),
                Question("What is the equation for measuring pressure?","P = x/t","P = F/A",
                    "P = mg","P = m/V",2)
            )
        ),
        Topic("Marvel Super Heroes","This quiz will test your knowledge of Marvel Super Heroes.",
        "It will ask about some questions based on the history, events, and general information about Marvel heroes.",
            listOf(
                Question("What is Spider-Man's secret identity?","Ben Parker","Ben Reilly",
                    "Peter Parker","Peter Pan",3),
                Question("Which superhero fights mainly with a shield?","Captain America",
                "Iron Man", "Captain Marvel","Star-Lord",1),
                Question("Who lead the organization S.H.I.E.L.D.?","Black Widow","Nick Fury",
                "Charles Xavier","Dr. Strange",2)
            )
        )
    )

    override fun getAllTopics(): List<Topic> {
        val topicsList : MutableList<Topic> = mutableListOf()
        for (i in topics.indices) {
            topicsList.add(Topic(topics[i].title, topics[i].shortDesc, topics[i].longDesc,
                topics[i].questions))
        }
        return topics
    }

    override fun getTopic(index: Int): Topic {
        return topics[index]
    }

    override fun getQuiz(topicNum: Int, index: Int): Quiz {
        return Quiz(
            topics[topicNum].questions[index].q,
            topics[topicNum].questions[index].a1,
            topics[topicNum].questions[index].a2,
            topics[topicNum].questions[index].a3,
            topics[topicNum].questions[index].a4,
            topics[topicNum].questions[index].correctAnswer,
        )
    }
}

object Singleton
class QuizApp : Application() {
    lateinit var topicRepository: TopicRepository

    companion object {
        private lateinit var instance: QuizApp

        fun getInstance(): QuizApp {
            synchronized(this) {
                if (!::instance.isInitialized) {
                    instance = QuizApp()
                }
                return instance
            }
        }
    }
    override fun onCreate() {
        super.onCreate()
        Log.i("QuizApp", "onCreate() loaded and running")
        instance = this

        topicRepository = MockTopicRepository()
    }
}