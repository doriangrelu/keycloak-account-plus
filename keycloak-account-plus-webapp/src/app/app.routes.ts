import { Routes } from '@angular/router';
import { authGuard } from './core/auth/auth.guard';
import { Infos } from '@features/account/infos/infos';
import { Account } from '@features/account/account';
import { Access } from '@features/account/access/access';
import { Parameters } from '@features/account/parameters/parameters';

export const routes: Routes = [
  {
    path: '',
    redirectTo: '/account/infos',
    pathMatch: 'full',
  },
  {
    path: 'account',
    component: Account,
    canActivate: [authGuard],
    children: [
      {
        path: 'infos',
        component: Infos,
      },
      {
        path: 'access',
        component: Access,
      },
      {
        path: 'parameters',
        component: Parameters,
      }
    ]
  }
];
