# SimpleChess Backend

Backend for the SimpleChess Server.

## API
Requests implemented in service:

### Create a game
    {POST /api/games, produces [application/json]}: createGame()
    
    { 
        "gameId": "4a0ffac6-b2d9-4d53-8573-0ec6ec1ccaab" 
    }
    
### Get all games 
    {GET /api/games, produces [application/json]}: getGames()
    
    [
        {
            "gameId": "4a0ffac6-b2d9-4d53-8573-0ec6ec1ccaab",
            "board": {
                "fen": "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1",
                "sideToMove": "WHITE"
            },
            "result": "ONGOING"
        }
    ]
    
### Get board
    {GET /api/games/{gameId}/board, produces [application/json]}: getBoardByGameId(UUID)
    
    {
        "fen": "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1",
        "sideToMove": "WHITE"
    }
    
### Move a piece
    {POST /api/games/{gameId}/move, consumes [application/json], produces [application/json]}: doMove(UUID,MoveDTO)
    
    {
        "from": "a2",
        "to": "a3"
    }
    
    {
        "gameId": "4a0ffac6-b2d9-4d53-8573-0ec6ec1ccaab",
        "board": {
            "fen": "rnbqkbnr/pppppppp/8/8/8/P7/1PPPPPPP/RNBQKBNR b KQkq - 0 1",
            "sideToMove": "BLACK"
        },
        "result": "ONGOING"
    }
  
### Get a specific game  
    {GET /api/games/{gameId}, produces [application/json]}: getGameById(UUID)
    
    {
        "gameId": "4a0ffac6-b2d9-4d53-8573-0ec6ec1ccaab",
        "board": {
            "fen": "rnbqkbnr/pppppppp/8/8/8/P7/1PPPPPPP/RNBQKBNR b KQkq - 0 1",
            "sideToMove": "BLACK"
        },
        "result": "ONGOING"
    }

See also: http://localhost:8080/swagger-ui.html
