import { Component } from '@angular/core';
import { RouterLink, RouterOutlet } from '@angular/router';
@Component({selector:'app-root',standalone:true,imports:[RouterLink,RouterOutlet],template:`
<header class="topbar"><a routerLink="/" class="brand"><span class="brand-icon">◎</span> Civic<span>AI</span></a><nav><a routerLink="/">Report an Issue</a><a routerLink="/track">Track Case</a><a routerLink="/officer">Officer Portal</a></nav></header>
<main class="shell"><router-outlet /></main><footer>Academic demonstration • AI suggestions require officer review • For emergencies, contact local emergency services.</footer>
`}) export class AppComponent {}
