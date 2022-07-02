import { Injectable } from '@angular/core';
import { BaseNewsService } from '../base-news.service';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { News } from '../news';
import { User } from '../User';


import { map } from 'rxjs/operators';
import { BasicAuthService } from './basic-auth.service';
import { AuthService } from './auth.service';

@Injectable()
export class AuthNewsService extends BaseNewsService {

  private _authService: AuthService;

  constructor(http: HttpClient) {
    super(http);
    this._authService = new BasicAuthService(http);
  }

  getAll(): Observable<News[]> {
    var authorization = sessionStorage.getItem('token')
    console.log("token",authorization)
    return this.http.get<any[]>(`${this._authService.getBaseUrl()}/news?Authorization=Bearer ${authorization}`, {headers: this._authService.getAuthHeaders()}).pipe(
      map(body => body.map(n => News.fromObject(n)))
    );
    // return this.http.get<any[]>(`${this._authService.getBaseUrl()}/news`, {headers: this._authService.getAuthHeaders()}).pipe(
    //   map(body => body.map(n => News.fromObject(n)))
    // );
  }

  getNewest(): Observable<News> {
    // return this.http.get<any>(`${this._authService.getBaseUrl()}/news/newest`, {headers: this._authService.getAuthHeaders()}).pipe(
    //   map(body => News.fromObject(body))
    // );
    var authorization = sessionStorage.getItem('token')
    console.log("token",authorization)
    return this.http.get<any>(`${this._authService.getBaseUrl()}/news/newest?Authorization=Bearer ${authorization}`, {headers: this._authService.getAuthHeaders()}).pipe(
      map(body => News.fromObject(body))
    );
  }

  create(headline: string, content: string): Observable<News> {
    var authorization = sessionStorage.getItem('token')
    console.log("token",authorization)
    return this.http.post<any>(`${this._authService.getBaseUrl()}/news?Authorization=Bearer ${authorization}`, {headline, content}).pipe(
      map(body => News.fromObject(body))
    );

    // return this.http.post<any>(`${this._authService.getBaseUrl()}/news`, {headline, content}, {headers: this._authService.getAuthHeaders()}).pipe(
    //   map(body => News.fromObject(body))
    // );
  }

  getUserList(noteId: string): Observable<User> {
    return this.http.get<any>(`${this._authService.getBaseUrl()}/getUserList?noteId=${noteId}`, {headers: this.defaultHeaders}).pipe(
      map(body => User.fromObject(body))
    );
  }

  deleteNote(noteId: string): Observable<News> {
    var authorization = sessionStorage.getItem('token')
    return this.http.post<any>(`${this._authService.getBaseUrl()}/deleteNote?Authorization=Bearer ${authorization}`, {noteId}).pipe(
      map(body => News.fromObject(body))
    );
  }

  updateUsernameOn(noteId: string,usernameOn:string[]): Observable<News> {
    var authorization = sessionStorage.getItem('token')
    return this.http.post<any>(`${this._authService.getBaseUrl()}/updateUsernameOn?Authorization=Bearer ${authorization}`, {noteId,usernameOn}).pipe(
      map(body => News.fromObject(body))
    );
  }

  set authService(value: AuthService) {
    this._authService = value;
  }
}
