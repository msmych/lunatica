// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-nocheck

import {
	RouteLocationNormalized,
	createRouter,
	createWebHistory,
	createWebHashHistory,
} from 'vue-router'

import { useConfigStore } from './state'

import { RouteName } from './types/common.types'

// TODO: Decide if we still want to use pr-verifier and effort
// Update pr-verifier repo then replace this
function isPrSpecificBuild(): boolean {
	return process.env.NODE_ENV === 'production' && !!process.env.VITE_APP_QA_ENV
}

// in order to pass id to page props - we need to write 'props: true,' after 'name' in route
const router = createRouter({
	history: isPrSpecificBuild()
		? createWebHashHistory(import.meta.env.BASE_URL)
		: createWebHistory(import.meta.env.BASE_URL),
	routes: [
		{
      path: '/',
      redirect: () => {
				return { path: '/login' }
			},
    },
		{
      path: '/login',
      name: RouteName.Login,
			meta: {
				title: 'Логин',
				hideForAuth: true
			},
      component: () => import('./pages/Login.vue')
    },
		{
      path: '/registration',
      name: RouteName.Registration,
			meta: {
				title: 'Регистрация',
				hideForAuth: true
			},
      component: () => import('./pages/Registration.vue')
    },
		{
			path: '/complaint-new',
			name: RouteName.ComplaintNew,
			meta: {
				title: 'Новое обращение',
				requiresAuth: true
			},
			component: () => import('./pages/ComplaintNew.vue')
		},
		{
			path: '/complaints',
			name: RouteName.Complaints,
			meta: {
				title: 'Обращения',
				requiresAuth: true
			},
			component: () => import('./pages/Complaints.vue')
		},
		{
			path: '/complaints/:id',
			name: RouteName.Complaint,
			meta: {
				title: 'Обращение',
				requiresAuth: true
			},
			component: () => import('./pages/Complaint.vue')
		},
		{
			path: '/settings',
			name: RouteName.Settings,
			meta: {
				title: 'Настройки',
				requiresAuth: true
			},
			component: () => import('./pages/Settings.vue')
		},
		{
			path: '/users',
			name: RouteName.Users,
			meta: {
				title: 'Пользователи',
				requiresAuth: true
			},
			component: () => import('./pages/Users.vue')
		},
	]
})

const addPrefixToTitle = (title: string) =>
	`Lunatica | ${title}`

export const addTitleToDocument = (to: RouteLocationNormalized, pageTitle?: string) => {
	if (pageTitle?.length) {
		document.title = addPrefixToTitle(pageTitle)
	} else {
		// This goes through the matched routes from last to first,
		// finding the closest route with a title.
		// eg. if we have /some/deep/nested/route and
		// some, /deep, and /nested have titles, nested's will be chosen.
		const nearestWithTitle = to.matched
			.slice()
			.reverse()
			.find(r => r.meta && r.meta.title)

		// If a route with a title was found,
		// set the document (page) title to that value.
		if (nearestWithTitle) {
			document.title = addPrefixToTitle(nearestWithTitle.meta.title as string)
		} else {
			document.title = `Lunatica`
		}
	}
}

router.beforeEach(async (to) => {
	addTitleToDocument(to)
})

router.beforeEach((to, from, next) => {
	if (to.matched.some(record => record.meta.requiresAuth)) {
    // this route requires auth, check if logged in
    // if not, redirect to login page.
    if (!useConfigStore().user.id) {
      next({ name: 'Login' })
    } else {
      next() // go to wherever I'm going
    }
  } else {
    next() // does not require auth, make sure to always call next()!
  }
});

export default router
