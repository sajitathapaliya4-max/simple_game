package com.example.simplegame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TicTacToeGame()
                }
            }
        }
    }
}

@Composable
fun TicTacToeGame() {
    val playerX = stringResource(R.string.player_x)
    val playerO = stringResource(R.string.player_o)
    
    var board by remember { mutableStateOf(List(9) { "" }) }
    var xIsNext by remember { mutableStateOf(true) }
    val winner = calculateWinner(board)
    val status = when {
        winner != null -> stringResource(R.string.winner_status, winner)
        board.all { it.isNotEmpty() } -> stringResource(R.string.draw_status)
        else -> stringResource(R.string.next_player_status, if (xIsNext) playerX else playerO)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.game_title),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )
        Text(
            text = status,
            fontSize = 20.sp,
            modifier = Modifier.padding(8.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        // 3x3 Grid
        for (row in 0 until 3) {
            Row {
                for (col in 0 until 3) {
                    val index = row * 3 + col
                    Square(
                        value = board[index],
                        playerX = playerX,
                        onClick = {
                            if (board[index].isEmpty() && winner == null) {
                                val newBoard = board.toMutableList()
                                newBoard[index] = if (xIsNext) playerX else playerO
                                board = newBoard
                                xIsNext = !xIsNext
                            }
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = {
            board = List(9) { "" }
            xIsNext = true
        }) {
            Text(stringResource(R.string.reset_game))
        }
    }
}

@Composable
fun Square(value: String, playerX: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .padding(4.dp)
            .background(Color.LightGray)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = value,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = if (value == playerX) Color.Blue else Color.Red
        )
    }
}

fun calculateWinner(squares: List<String>): String? {
    val lines = listOf(
        listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8), // Rows
        listOf(0, 3, 6), listOf(1, 4, 7), listOf(2, 5, 8), // Cols
        listOf(0, 4, 8), listOf(2, 4, 6)             // Diagonals
    )
    for (line in lines) {
        val (a, b, c) = line
        if (squares[a].isNotEmpty() && squares[a] == squares[b] && squares[a] == squares[c]) {
            return squares[a]
        }
    }
    return null
}
