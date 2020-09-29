import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { take } from 'rxjs/operators';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot } from '@angular/router';
import { GameService } from './game.service';


@Injectable({
  providedIn: 'root'
})
export class GameResolver implements Resolve<any> {

  constructor(private facade: GameService) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<any> | Promise<any> | any {
    const gameId = route.paramMap.get('id');

    return this.facade.fetchGame(gameId).pipe(
      take(1)
    );
  }

}
