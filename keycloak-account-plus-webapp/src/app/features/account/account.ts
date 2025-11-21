import {Component} from '@angular/core';
import {RouterLink, RouterLinkActive, RouterOutlet} from '@angular/router';
import {MatTabLink, MatTabNav, MatTabNavPanel} from '@angular/material/tabs';

@Component({
  selector: 'app-account',
  imports: [
    RouterOutlet,
    MatTabNav,
    MatTabLink,
    RouterLink,
    RouterLinkActive,
    MatTabNavPanel
  ],
  templateUrl: './account.html',
  styleUrl: './account.css',
})
export class Account {

  protected links: {
    link: string,
    label: string
  }[] = [
    {
      label: 'Mes informations',
      link: '/account/infos'
    },
    {
      label: 'Paramètres',
      link: '/account/parameters'
    },
    {
      label: 'Accès à mon compte',
      link: '/account/access'
    },
  ];

}
