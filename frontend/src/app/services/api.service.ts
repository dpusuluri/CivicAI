import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
export interface CivicCase {id:string;trackingCode:string;accessToken:string|null;description:string;latitude:number;longitude:number;category:string;department:string;status:string;priority:string;aiObservation:string;aiConfidence:number;hasPhoto:boolean;resolutionNote:string|null;createdAt:string;history:{status:string;actor:string;reason:string;createdAt:string}[]}
@Injectable({providedIn:'root'}) export class ApiService {
 private http=inject(HttpClient); private base='/api';
 submit(description:string,lat:number,lng:number,photo:File|null):Observable<CivicCase>{const data=new FormData();data.append('case',new Blob([JSON.stringify({description,latitude:lat,longitude:lng})],{type:'application/json'}));if(photo)data.append('photo',photo);return this.http.post<CivicCase>(`${this.base}/cases`,data);}
 track(code:string,token:string):Observable<CivicCase>{return this.http.get<CivicCase>(`${this.base}/cases/track/${encodeURIComponent(code)}?token=${encodeURIComponent(token)}`);}
 private options(auth:string){return {headers:new HttpHeaders({Authorization:'Basic '+auth})};}
 list(auth:string):Observable<CivicCase[]>{return this.http.get<CivicCase[]>(`${this.base}/officer/cases`,this.options(auth));}
 update(id:string,status:string,reason:string,resolutionNote:string,department:string,auth:string):Observable<CivicCase>{return this.http.put<CivicCase>(`${this.base}/officer/cases/${id}`,{status,reason,resolutionNote,department},this.options(auth));}
 report(id:string,auth:string):Observable<string>{return this.http.get(`${this.base}/officer/cases/${id}/report`,{...this.options(auth),responseType:'text'});}
}
