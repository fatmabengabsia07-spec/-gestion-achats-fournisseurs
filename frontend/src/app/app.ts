import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { NavbarComponent } from './components/navbar/navbar.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, NavbarComponent],
  template: `
    <div class="app-shell">
      <app-navbar></app-navbar>
      <main class="app-main">
        <router-outlet></router-outlet>
      </main>
    </div>
  `
})
export class AppComponent {}