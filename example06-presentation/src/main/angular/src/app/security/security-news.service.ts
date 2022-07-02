import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { News } from '../news';
import { User } from '../User';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { BaseNewsService } from '../base-news.service';
import { environment as env } from '../../environments/environment';

@Injectable()
export class SecurityNewsService extends BaseNewsService {

  constructor(http: HttpClient) {
    super(http);
  }

  getAll(): Observable<News[]> {
    return this.http.get<any[]>(`${env.apiUrl}/security/news`, {headers: this.defaultHeaders}).pipe(
      map(body => body.map(n => News.fromObject(n)))
    );
  }

  getNewest(): Observable<News> {
    return this.http.get<any>(`${env.apiUrl}/security/news/newest`, {headers: this.defaultHeaders}).pipe(
      map(body => News.fromObject(body))
    );
  }

  getUserList(noteId: string): Observable<User> {
      return this.http.get<any>(`${env.apiUrl}/getUserList?noteId=${noteId}`, {headers: this.defaultHeaders}).pipe(
        map(body => User.fromObject(body))
      );
    }

  create(headline: string, content: string): Observable<News> {
    return this.http.post<any>(`${env.apiUrl}/security/news`, {headline, content}, {headers: this.defaultHeaders}).pipe(
      map(body => News.fromObject(body))
    );
  }

  deleteNote(noteId: string): Observable<News> {
    return this.http.post<any>(`${env.apiUrl}/deleteNote`, {noteId}, {headers: this.defaultHeaders})
  }

  updateUsernameOn(noteId: string,usernameOn:string[]): Observable<News> {
    return this.http.post<any>(`${env.apiUrl}/updateUsernameOn`, {noteId,usernameOn}, {headers: this.defaultHeaders})
  }
}
