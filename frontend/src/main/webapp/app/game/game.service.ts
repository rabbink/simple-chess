import { Injectable, OnDestroy } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, Subject } from 'rxjs';
import { take, tap } from 'rxjs/operators';
import { Router } from '@angular/router';

export interface Game {
  gameId: string;
  board: Board;
  result: string;
}

export interface Board {
  fen: string;
  sideToMove: string;
}

export interface Move {
  from: string;
  to: string;
}

@Injectable({
  providedIn: 'root'
})
export class GameService implements OnDestroy {

  private unsubscribe: Subject<any>;

  constructor(private http: HttpClient, private router: Router) {
        this.unsubscribe = new Subject();
  }

  fetchGame(gameId: string): Observable<Game> {
    return this.http.get<Game>(`api/games/${gameId}`);
  }

  createGame(): void {
    this.http.post('api/games', null).pipe(
      tap((response: {gameId: string})  => {
        this.router.navigate(['game', response.gameId]);
      }),
      take(1)
    ).subscribe();
  }

  doMove(gameId: string, move: Move): Observable<Game> {
    return this.http.post<Game>(`api/games/${gameId}/move`, move);
  }

  ngOnDestroy(): void {
    this.unsubscribe.next();
    this.unsubscribe.complete();
  }

}
