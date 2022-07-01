import { Component, Input ,Output,EventEmitter } from '@angular/core';
import { News } from '../../news';
import { NewsService } from '../news.service';
// import { AuthNewsService } from '../../auth/auth-news.service'

@Component({
  selector: 'wt2-news-list',
  templateUrl: './news-list.component.html',
  styleUrls: ['./news-list.component.sass'],
  providers: [NewsService]
})
export class NewsListComponent {

  constructor(protected newsService: NewsService) {
  }

  @Output()
  public created = new EventEmitter();

  @Input()
  public news: News[] = [];

  get reversedNews(): News[] {
    return this.news.slice().reverse();
  }
  
  doDelete(a): void {
    console.log('delete',a)
    this.newsService.deleteNote(a.id).subscribe({
      next: () => {
      },
      error: console.error
    });
  }

  doAssign(a): void {
    console.log(a)
  }
  
}
