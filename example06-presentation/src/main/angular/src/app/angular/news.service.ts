import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { News } from '../news';
import { User } from '../User';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { BaseNewsService } from '../base-news.service';
import { environment as env } from '../../environments/environment';

@Injectable()
export class NewsService extends BaseNewsService {

  constructor(http: HttpClient) {
    super(http);
  }

  getAll(): Observable<News[]> {
    return this.http.get<any[]>(`${env.apiUrl}/news`, {headers: this.defaultHeaders}).pipe(
      map(body => body.map(n => News.fromObject(n)))
    );
  }

  getNewest(): Observable<News> {
    return this.http.get<any>(`${env.apiUrl}/news/newest`, {headers: this.defaultHeaders}).pipe(
      map(body => News.fromObject(body))
    );
  }

  create(headline: string, content: string): Observable<News> {
    return this.http.post<any>(`${env.apiUrl}/news`, {headline, content}, {headers: this.defaultHeaders}).pipe(
      map(body => News.fromObject(body))
    );
  }
  
  getUserList(noteId: string): Observable<User> {
    var authorization = sessionStorage.getItem('token')
    return this.http.get<any>(`${env.apiUrl}/auth/jwt/getUserList?Authorization=Bearer ${authorization}&noteId=${noteId}`, {headers: this.defaultHeaders}).pipe(
      map(body => User.fromObject(body))
    );
  }

  deleteNote(noteId: string): Observable<News> {
    var authorization = sessionStorage.getItem('token')
    return this.http.post<any>(`${env.apiUrl}/auth/jwt/deleteNote?Authorization=Bearer ${authorization}`, {noteId}, {headers: this.defaultHeaders}).pipe(
      map(body => News.fromObject(body))
    );
  }

  updateUsernameOn(noteId: string,usernameOn:string[]): Observable<News> {
    var authorization = sessionStorage.getItem('token')
    return this.http.post<any>(`${env.apiUrl}/auth/jwt/updateUsernameOn?Authorization=Bearer ${authorization}`, {noteId,usernameOn}, {headers: this.defaultHeaders}).pipe(
      map(body => News.fromObject(body))
    );
  }
}
