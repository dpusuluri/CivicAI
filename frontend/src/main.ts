import { bootstrapApplication } from '@angular/platform-browser';
import { provideHttpClient } from '@angular/common/http';
import { provideRouter } from '@angular/router';
import { AppComponent } from './app/app.component';
import { ReportComponent } from './app/pages/report.component';
import { TrackComponent } from './app/pages/track.component';
import { OfficerComponent } from './app/pages/officer.component';
bootstrapApplication(AppComponent,{providers:[provideHttpClient(),provideRouter([{path:'',component:ReportComponent},{path:'track',component:TrackComponent},{path:'officer',component:OfficerComponent},{path:'**',redirectTo:''}])]}).catch(console.error);
