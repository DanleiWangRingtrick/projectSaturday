import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { News } from './news';
import { User } from './User';

export abstract class BaseNewsService {
  protected defaultHeaders = new HttpHeaders({
    'Content-Type': 'application/json'
  });

  protected constructor(protected http: HttpClient) {
  }

  abstract getNewest(): Observable<News>;

  abstract getAll(): Observable<News[]>;

  abstract getUserList(noteId: string): Observable<User>;

  abstract create(headline: string, content: string): Observable<News>;

  abstract deleteNote(noteId: string): Observable<News>;
}

