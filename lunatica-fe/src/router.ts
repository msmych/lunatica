import {
	RouteLocationNormalized,
	createRouter,
	createWebHistory,
	createWebHashHistory,
} from 'vue-router'

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
      name: RouteName.Login,
			meta: {
				title: 'Login'
			},
      component: () => import('./pages/Login.vue')
    },
		{
      path: '/registration',
      name: RouteName.Registration,
			meta: {
				title: 'Registration'
			},
      component: () => import('./pages/Registration.vue')
    },
		{
			path: '/home',
			name: RouteName.Home,
			meta: {
				title: 'Home'
			},
			component: () => import('./pages/Home.vue')
		},
		{
			path: '/dashboard',
			name: RouteName.Dashboard,
			meta: {
				title: 'Dashboard'
			},
			component: () => import('./pages/Dashboard.vue')
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

	

export default router
