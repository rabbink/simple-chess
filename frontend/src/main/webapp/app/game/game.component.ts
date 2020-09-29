import { Component, OnDestroy, OnInit } from '@angular/core';
import { Game, GameService } from './game.service';
import { ActivatedRoute } from '@angular/router';
import { catchError, distinctUntilChanged, finalize, switchMap, take, takeUntil, tap } from 'rxjs/operators';
import { EMPTY, interval, Subject } from 'rxjs';

declare var ChessBoard: any;

@Component({
  selector: 'app-game',
  templateUrl: './game.component.html',
  styleUrls: ['./game.component.css']
})
export class GameComponent implements OnInit, OnDestroy {

  game: Game;

  board: any;

  displayAlert = false;
  alertMessage = '';

  move = { from: "", to: "" };

  private readonly unsubscribe: Subject<any>;

  constructor(private gameService: GameService, private route: ActivatedRoute) {
    this.unsubscribe = new Subject();
  }

  ngOnInit(): void {
    this.game = this.route.snapshot.data.game;

    this.board = ChessBoard('board1', {
      position: this.game.board.fen
    })

    const source = interval(3000);

    source.pipe(
      switchMap(source => this.gameService.fetchGame(this.game.gameId)),
      distinctUntilChanged((a, b) => {
        return JSON.stringify(a) === JSON.stringify(b);
      }),
      tap(game => {
        this.game = game;
        this.board.position(game.board.fen);
      }),
      takeUntil(this.unsubscribe)
    ).subscribe();
  }

  doMove() {
    this.displayAlert = false;

    this.gameService.doMove(this.game.gameId, this.move).pipe(
      tap(updatedGame => this.game = updatedGame),
      finalize(() => this.move = {from: "", to: ""}),
      catchError(err => {
        console.log(err);
        this.alertMessage = err.error.message;
        this.displayAlert = true;
        return EMPTY;
      }),
      take(1)
    ).subscribe();
  }

  ngOnDestroy(): void {
    this.unsubscribe.next();
    this.unsubscribe.complete();
  }
}
