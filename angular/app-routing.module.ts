
import { NgModule } from '@angular/core';
import { Routes, RouterModule, RouteReuseStrategy } from '@angular/router';
import { P404Component } from './components/p404/p404.component';
import { IndexComponent } from './components/index/index.component';
import { ComicsComponent } from './components/comics/comics.component';
import { CgsComponent } from './components/cgs/cgs.component';
import { ComicComponent } from './components/comic/comic.component';
import { SimpleReuseStrategy } from './SimpleReuseStrategy';


const routes: Routes = [
    { path: '', component: IndexComponent, pathMatch: 'full', data: { keep: true } },
    { path: 'index', component: IndexComponent, data: { keep: true } },
    { path: 'cgs', component: CgsComponent, data: { keep: true } },
    { path: 'comics/:type', component: ComicsComponent, data: { keep: true } },
    { path: 'comic', component: ComicComponent, data: { keep: true } },
    { path: '**', component: P404Component, data: { keep: true } }
];

@NgModule({
    imports: [RouterModule.forRoot(routes, { useHash: true })],
    exports: [RouterModule],
    providers: [
        { provide: RouteReuseStrategy, useClass: SimpleReuseStrategy }
    ]
})
export class AppRoutingModule { }