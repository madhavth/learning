package com.madhavth.firebaselearning.CustomViews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

enum class TURN{
    PLAYER2, PLAYER1
}

fun TURN.change(): TURN {
    return if(this == TURN.PLAYER1)
        TURN.PLAYER2
    else
        TURN.PLAYER1
}


class MyTicTacView(context: Context, attributeSet: AttributeSet): View(context, attributeSet)
{
    private val paintLine = Paint()
    private val paintBG = Paint()
    private var ticTacLayout= TicTacLayout(0,0,0,0)
    private var paintText = Paint()
    private var paintWin = Paint()
    private var turn  =TURN.PLAYER1
    private var count = 0
    private var winner: TURN? = null
    private var winCondition = WinCondition(0,0,0,0,0,0,0,0)

    private var gameStopped = false

    //could be better like game board List<List<>> i.e. [["x","o","x"],["","",""],["","",""]]
    // but  too far too late to realize this easier way andd too lazy to edit everyyyyy thing
    private var mapDrawInfos = HashMap<Points,DrawInfo>()

    private var tempLine = Points(0f,0f)

    private val Int.dp: Float
        get() {
        return this * resources.displayMetrics.density
        }


    init{
        paintLine.apply {
            isAntiAlias= true
            color = Color.BLACK
            strokeWidth = 12f
        }

        paintText.apply {
            isAntiAlias = true
            color = Color.YELLOW
            textSize = 75.dp
        }

        paintBG.apply {
            isAntiAlias = true
            strokeWidth = (right -left).toFloat()
        }

        paintWin.apply {
            isAntiAlias= true
            strokeWidth = 8f
            color = Color.WHITE
        }

        //init test for now
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        ticTacLayout.apply {
            this.left = left
            this.right = right
            this.top = top
            this.bottom = bottom
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

//        //draw rect
//        canvas?.drawRect(left.toFloat(), top.toFloat(),
//            right.toFloat(), bottom.toFloat(),paintBG)

        //draw 4 lines for game to be held
        canvas?.drawLine(ticTacLayout.right/3f, ticTacLayout.top.toFloat(),
            ticTacLayout.right/3f, ticTacLayout.bottom.toFloat(),paintLine
        )
        canvas?.drawLine(2 * ticTacLayout.right/3f, ticTacLayout.top.toFloat(),
            2* ticTacLayout.right/3f, ticTacLayout.bottom.toFloat(),paintLine
        )
        canvas?.drawLine(left.toFloat(), bottom/3f,right.toFloat(),  bottom/3f, paintLine)
        canvas?.drawLine(ticTacLayout.left.toFloat(), 2* ticTacLayout.bottom/3f, ticTacLayout.right.toFloat(), 2* ticTacLayout.bottom/3f, paintLine)

        mapDrawInfos.forEach{
            Timber.d("mapDrawInfo mapping $it")
            if(it.value.points.x !=0f && it.value.points.y != 0f)
            {
                if(it.value.player == TURN.PLAYER1)
                {
                    paintText.color = Color.YELLOW
                }

                else
                {
                    paintText.color = Color.BLUE
                }

                Timber.d("map Index  ${it.key}=> Points ${it.value.points}, Player = ${it.value.player}")
                canvas?.drawText(
                    if(it.value.player == TURN.PLAYER1)
                        "O" else "X", it.value.points.x-width/12, it.value.points.y+height/12, paintText)
            }
        }

        //todo: optimize this long line of code

        var possibleSolution = mutableListOf<MutableMap<Points,DrawInfo>>()

        val diagonal1 = mapDrawInfos.filter {
            it.key.x == it.key.y && it.value.player == TURN.PLAYER1
        }

        val diagonal2 = mapDrawInfos.filter {
            it.key.x == it.key.y && it.value.player == TURN.PLAYER2
        }

        val diagonal3 = mapDrawInfos.filter {
            it.key.x + it.key.y == 2f && it.value.player == TURN.PLAYER1
        }

        val diagonal4 = mapDrawInfos.filter {
            it.key.x + it.key.y == 2f && it.value.player == TURN.PLAYER2
        }

        possibleSolution.add(diagonal1.toMutableMap())
        possibleSolution.add(diagonal2.toMutableMap())
        possibleSolution.add(diagonal3.toMutableMap())
        possibleSolution.add(diagonal4.toMutableMap())

        for(i in 0..2) {

            val rowCheck1 = mapDrawInfos.filter {
                it.key.x == i.toFloat() && it.value.player == TURN.PLAYER1
            }

            val rowCheck2 = mapDrawInfos.filter {
                it.key.x == i.toFloat() && it.value.player == TURN.PLAYER2
            }

            val colCheck1 = mapDrawInfos.filter {
                it.key.y == i.toFloat() && it.value.player == TURN.PLAYER1
            }

            val colCheck2 = mapDrawInfos.filter {
                it.key.y == i.toFloat() && it.value.player == TURN.PLAYER2
            }

            possibleSolution.add(rowCheck1.toMutableMap())
            possibleSolution.add(rowCheck2.toMutableMap())
            possibleSolution.add(colCheck1.toMutableMap())
            possibleSolution.add(colCheck2.toMutableMap())
        }


        var playerWinner=  mutableMapOf<Points, DrawInfo>()
        try{
            playerWinner = possibleSolution.filter {
                it.size == 3
            }.first()
        }
        catch (e: Exception)
        {

        }


        if(playerWinner.isNotEmpty())
        {
            winner  = playerWinner.map {
                it.value.player
            }.first()
        }

        if (winner!=null)
        {
            val fPoints = playerWinner.values
            var pointsLine = mutableListOf<Points>()

            fPoints.forEach {
                val xF  = it.points.x
                val xY = it.points.y

                pointsLine.add(Points(xF,xY))
            }

            var minXY =pointsLine.minBy { it.x }!!
            var maxXY = pointsLine.maxBy { it.x }!!

            Timber.d("pointsLine are $minXY, $maxXY")
            if(minXY.x == maxXY.x)
            {
                minXY = pointsLine.minBy { it.y }!!
                maxXY = pointsLine.maxBy { it.y }!!
            }

            canvas?.drawLine(minXY.x,minXY.y, maxXY.x,maxXY.y,paintLine)

            Toast.makeText(context
            , "winner is $winner",
            Toast.LENGTH_SHORT).show()

            gameStopped =!gameStopped

            CoroutineScope(Dispatchers.IO).launch {
                gameOver()
            }
        }

        else if (count == 9 && winner == null)
        {
            count = 0
            Toast.makeText(context
            , "it's a drawwww!!! you are both terrible or equally good or both congrats",
            Toast.LENGTH_LONG).show()
            gameStopped =!gameStopped

            CoroutineScope(Dispatchers.IO).launch {
                gameOver()
            }
        }
    }

    suspend fun gameOver()
    {
        delay(1500)
        this@MyTicTacView.invalidate()
        winner =null
        count = 0
        mapDrawInfos.clear()
        gameStopped =!gameStopped
    }



    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val temp =Points(0f,0f)
        val textXY = Points(0f,0f)

        if(event?.action == MotionEvent.ACTION_DOWN && !gameStopped)
        {
            //for getting key, value for map

            //setting centre for selected area
            when(event.rawX.toInt())
            {
                in left..right/3 -> {
                    textXY.x = (left+right/3)/2f
                    temp.x = 0f
                }

                in right/3..2*right/3 -> {
                    textXY.x = (right)/2f
                    temp.x = 1f
                }

                in 2*right/3..right -> {
                    textXY.x = 5*right/6f
                    temp.x = 2f
                }
            }

            //for setting Y
            when(event.rawY.toInt())
            {
                in top..bottom/3 -> {
                    textXY.y = (top+bottom/3)/2f
                    temp.y= 0f
                }

                in bottom/3..2*bottom/3 -> {
                    textXY.y = (bottom)/2f
                    temp.y = 1f
                }

                in 2*bottom/3..bottom -> {
                    textXY.y = 5*bottom/6f
                    temp.y = 2f
                }

            }

            Timber.d("mapDrawInfo onTouch $temp -> $textXY, $turn")

            if(mapDrawInfos[temp]==null)
            {
                mapDrawInfos[temp] = DrawInfo(textXY, turn)
                count++
                turn = if(turn == TURN.PLAYER1) TURN.PLAYER2 else TURN.PLAYER1
                Timber.d("Testing left top bottom right are $left, $top, $bottom, $right")
                Timber.d("Testing rawXY are ${event.rawX}, ${event.rawY}")
                Timber.d("Testing textXY are $textXY")
                invalidate()
            }
        }
        return false
    }
}

data class TicTacLayout(
    var left: Int,
    var right:Int,
    var top: Int,
    var bottom: Int
)

data class Points(
    var x: Float,
    var y: Float
)

data class DrawInfo(
    var points: Points,
    var player: TURN
)

data class WinCondition(
    var diagonal1: Int,
    var diagonal2: Int,
    var diagonal11: Int,
    var diagonal22: Int,
    var row: Int,
    var column: Int,
    var row2: Int,
    var column2: Int
)
