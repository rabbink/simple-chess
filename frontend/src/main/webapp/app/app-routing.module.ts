import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { GameComponent } from './game/game.component';
import { BaseComponent } from './base/base.component';
import { GameResolver } from './game/game-resolver.service';

const routes: Routes = [
	{
		path: '',
		component: BaseComponent
	},
  {
    path: 'game/:id',
    component: GameComponent,
    resolve: {
      game: GameResolver
    }
  },
];

@NgModule({
	imports: [
		RouterModule.forRoot(routes, {useHash: true})
	],
	exports: [RouterModule]
})
export class AppRoutingModule {
}
