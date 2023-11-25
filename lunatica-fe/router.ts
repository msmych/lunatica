import {
	RouteLocationNormalized,
	RouteLocation,
	createRouter,
	createWebHistory,
	createWebHashHistory,
	parseQuery,
	stringifyQuery
} from 'vue-router'
import { updateProfilingHeadersRouteUuid } from '@politico/profiling-headers'
import { conditionalHashRouterAndBaseUrl } from '@politico/pr-verifier'

import { RouteName, ModalsRoutes } from '@/types/common.types'
import { appLayoutStyles } from '@/styles'
// import { appLayoutStyles } from '@/styles/_layout.module.scss'
import { DELTA_FE_CONFIG } from './constants'
import qs from 'qs'

import { featEnabled, FeatureToggle } from './helpers/userHelpers'

// TODO: Decide if we still want to use pr-verifier and effort
// Update pr-verifier repo then replace this
function isPrSpecificBuild(): boolean {
	return process.env.NODE_ENV === 'production' && !!process.env.VITE_APP_QA_ENV
}

const namespaceTitle = configNamespace => {
	return configNamespace === 'fdaiq'
		? 'Life Sciences'
		: configNamespace === 'chemicalsiq'
		? 'Chemicals'
		: ''
}

// in order to pass id to page props - we need to write 'props: true,' after 'name' in route
const router = createRouter({
	history: isPrSpecificBuild()
		? createWebHashHistory(import.meta.env.BASE_URL)
		: createWebHistory(import.meta.env.BASE_URL),
	routes: [
		{
			path: '/',
			name: RouteName.Home,
			component: () => import(/* webpackChunkName: "homePage" */ './pages/HomePage.vue')
		},
		{
			path: '/account/profile',
			name: RouteName.AccountProfile,
			meta: {
				title: 'My Profile'
			},
			component: () =>
				import(
					/* webpackChunkName: "userProfilePage" */ './pages/account/UserProfilePage.vue'
				)
		},
		{
			path: '/account/settings',
			name: RouteName.AccountSettings,
			meta: {
				title: 'Settings'
			},
			component: () =>
				import(
					/* webpackChunkName: "accountSettingsPage" */ './pages/account/SettingsPage.vue'
				)
		},
		{
			path: '/notifications',
			name: RouteName.Notifications,
			meta: {
				title: 'Notifications'
			},
			component: () =>
				import(
					/* webpackChunkName: "notificationsPage" */ './pages/NotificationsPage.vue'
				)
		},
		{
			path: '/search',
			name: RouteName.Search,
			meta: {
				title: 'Search'
			},
			component: () =>
				import(
					/* webpackChunkName: "searchPageWrapper" */ './pages/SearchPageWrapper.vue'
				)
		},
		{
			path: '/documents',
			name: RouteName.SourceDocuments,
			component: () =>
				import(
					/* webpackChunkName: "documentsPageWrapper" */ './pages/DocumentsPageWrapper.vue'
				)
		},
		{
			path: '/regulations',
			children: [
				{
					path: '',
					name: RouteName.Regulations,
					meta: {
						title: 'Federal Regulatory Explorer'
					},
					component: () =>
						import(
							/* webpackChunkName: "placeholder" */ '@/components/search/Placeholder.vue'
						)
				},
				{
					path: 'states',
					name: RouteName.StateRegulations,
					meta: {
						title: 'State Regulatory Explorer'
					},
					component: () =>
						import(
							/* webpackChunkName: "stateRegulationsMap" */ './pages/StateRegulationsMap.vue'
						)
				},
				{
					path: 'states/rule/:id',
					name: RouteName.StateRegulationRule,
					props: true,
					meta: {
						title: 'State Regulatory Explorer'
					},
					component: () =>
						import(
							/* webpackChunkName: "StateRegulationPage" */ './pages/StateRegulationPage.vue'
						)
				},
				{
					path: 'rule/:id',
					name: RouteName.RegulationsRule,
					props: true,
					meta: {
						title: 'Rule'
					},
					component: () =>
						import(/* webpackChunkName: "rulePage" */ './pages/RulePage.vue')
				},
				{
					path: 'document/:type?/:id',
					name: RouteName.RegulationsDocument,
					props: true,
					meta: {
						title: 'Document'
					},
					component: () =>
						import(/* webpackChunkName: "documentPage" */ './pages/DocumentPage.vue')
				}
			],
			meta: {
				title: 'Regulatory Explorer',
				isExplorer: true
			},
			component: () =>
				import(
					/* webpackChunkName: "regulatoryExplorerPageWrapper" */ './pages/RegulatoryExplorerPageWrapper.vue'
				)
		},
		{
			path: '/legislations',
			children: [
				{
					path: '',
					name: RouteName.Legislations,
					component: () =>
						import(
							/* webpackChunkName: "placeholder" */ '@/components/search/Placeholder.vue'
						)
				},
				{
					path: 'legislation/:id',
					name: RouteName.LegislationsDocument,
					props: true,
					meta: {
						title: 'Legislation'
					},
					component: () =>
						import(/* webpackChunkName: "documentPage" */ './pages/LegislationPage.vue')
				}
			],
			meta: {
				title: 'Legislative Tracker',
				isExplorer: true
			},
			component: () =>
				import(
					/* webpackChunkName: "LegislativeExplorerPageWrapper" */ './pages/LegislativeExplorerPageWrapper.vue'
				)
		},
		{
			path: '/legislation/:id/documents',
			name: RouteName.LegislationDocuments,
			component: () =>
				import(
					/* webpackChunkName: "legislationDocumentsPageWrapper" */ './pages/LegislationDocumentsPageWrapper.vue'
				)
		},
		{
			path: '/calendar',
			children: [
				{
					path: ':isHeadingFocus?/:isUpcomingEvent?',
					name: RouteName.Calendar,
					props: true,
					component: () =>
						import(
							/* webpackChunkName: "placeholder" */ '@/components/search/Placeholder.vue'
						)
				},
				{
					path: 'event/:id',
					name: RouteName.CalendarEvent,
					props: true,
					meta: {
						title: 'Event'
					},
					component: () =>
						import(/* webpackChunkName: "eventPage" */ './pages/EventPage.vue')
				}
			],
			meta: {
				title: 'Calendar',
				isExplorer: true
			},
			component: () =>
				import(
					/* webpackChunkName: "calendarPageWrapper" */ './pages/CalendarExplorerPageWrapper.vue'
				)
		},
		{
			path: '/collections',
			name: RouteName.Collections,
			meta: {
				title: 'Collections'
			},
			component: () =>
				import(/* webpackChunkName: "collectionsPage" */ './pages/CollectionsPage.vue')
		},
		{
			path: '/watch-list/:isHeadingFocus?',
			name: RouteName.WatchList,
			meta: {
				title: 'Watch List'
			},
			component: () =>
				import(/* webpackChunkName: "WatchListPage" */ './pages/WatchListPage.vue')
		},
		{
			path: '/webinars/:isHeadingFocus?',
			name: RouteName.Webinars,
			meta: {
				title: 'Webinars'
			},
			component: () =>
				import(/* webpackChunkName: "WebinarsPage" */ './pages/WebinarsPage.vue')
		},
		{
			path: '/analysis/:isHeadingFocus?',
			name: RouteName.Analysis,
			meta: {
				title: 'Analysis'
			},
			component: () =>
				import(/* webpackChunkName: "analysisPage" */ './pages/AnalysisPage.vue')
		},
		{
			path: '/news/:isHeadingFocus?',
			name: RouteName.News,
			meta: {
				title: 'Politico News'
			},
			component: () => import(/* webpackChunkName: "newsPage" */ './pages/NewsPage.vue')
		},
		{
			path: '/external-news/:isHeadingFocus?',
			name: RouteName.ExternalNews,
			meta: {
				title: 'Around The Web'
			},
			component: () =>
				import(/* webpackChunkName: "externalNewsPage" */ './pages/ExternalNewsPage.vue')
		},
		{
			path: '/ee-news/:isHeadingFocus?',
			name: RouteName.EENews,
			meta: {
				title: 'E&E News'
			},
			component: () =>
				import(/* webpackChunkName: "eeNewsPage" */ './pages/EENewsPage.vue')
		},
		{
			path: '/tracked/:isHeadingFocus?',
			name: RouteName.Tracked,
			meta: {
				title: 'Tracking'
			},
			component: () =>
				import(/* webpackChunkName: "trackedPage" */ './pages/TrackedPage.vue')
		},
		{
			path: '/article/:id+',
			name: RouteName.Article,
			props: true,
			meta: {
				title: 'Article'
			},
			component: () =>
				import(/* webpackChunkName: "articlePage" */ './pages/ArticlePage.vue')
		},
		{
			path: '/collection/:id',
			name: RouteName.Collection,
			props: true,
			meta: {
				title: 'Collection'
			},
			component: () =>
				import(/* webpackChunkName: "collectionPage" */ './pages/CollectionPage.vue')
		},
		{
			path: '/curated-collection/:id',
			name: RouteName.CuratedCollection,
			props: true,
			meta: {
				title: 'Curated Collection'
			},
			component: () =>
				import(
					/* webpackChunkName: "curatedCollectionsPage" */ './pages/CuratedCollectionsPage.vue'
				)
		},
		{
			// Allow both "/document/DOC-ID-HERE" and "/document/doc-type/DOC-ID-HERE"
			path: '/document/:type?/:id',
			name: RouteName.Document,
			props: true,
			meta: {
				title: 'Document'
			},
			component: () =>
				import(/* webpackChunkName: "documentPage" */ './pages/DocumentPage.vue')
		},
		{
			path: '/document/:type?/:id/version/:versionId',
			name: RouteName.DocumentVersion,
			props: true,
			meta: {
				title: 'Document'
			},
			component: () =>
				import(/* webpackChunkName: "documentPage" */ './pages/DocumentPage.vue')
		},
		{
			path: '/event/:id',
			name: RouteName.Event,
			props: true,
			meta: {
				title: 'Event'
			},
			component: () => import(/* webpackChunkName: "eventPage" */ './pages/EventPage.vue')
		},
		{
			path: '/legislation/:id',
			name: RouteName.Legislation,
			props: true,
			meta: {
				title: 'Legislation'
			},
			component: () =>
				import(/* webpackChunkName: "documentPage" */ './pages/LegislationPage.vue')
		},
		{
			path: '/rule/:id',
			name: RouteName.Rule,
			props: true,
			meta: {
				title: 'Rule'
			},
			component: () => import(/* webpackChunkName: "rulePage" */ './pages/RulePage.vue')
		},
		{
			path: '/states/rule/:id',
			name: RouteName.StateRule,
			props: true,
			meta: {
				title: 'State Rule'
			},
			component: () =>
				import(
					/* webpackChunkName: "StateRegulationPage" */ './pages/StateRegulationPage.vue'
				)
		},
		{
			path: '/topic/:topicSearchFieldSlug?/:id',
			name: RouteName.Topic,
			props: true,
			meta: {
				title: 'Topic'
			},
			component: () => import(/* webpackChunkName: "topicPage" */ './pages/TopicPage.vue')
		},
		{
			path: '/topics',
			name: RouteName.Topics,
			props: true,
			meta: { title: 'Topic Explorer' },
			component: () =>
				import(/* webpackChunkName: "topicsPage" */ './pages/TopicsPage.vue')
		},
		{
			path: '/ask-the-experts',
			meta: {
				title: 'Ask The Experts'
			},
			name: RouteName.AskTheExperts,
			children: [
				{
					path: ':id',
					name: RouteName.AskTheExpertsDetails,
					component: () =>
						import(
							/* webpackChunkName: "AskTheExpertsDetails" */ '@/components/suggestTopic/AskTheExpertsDetails.vue'
						)
				}
			],
			component: () =>
				import(
					/* webpackChunkName: "AskTheExpertsPage" */ './pages/AskTheExpertsPage.vue'
				)
		},
		{
			path: '/welcome',
			name: RouteName.Welcome,
			props: true,
			meta: {
				title: 'Welcome to AgencyIQ',
				layout: 'Plain'
			},
			component: () =>
				import(/* webpackChunkName: "WelcomePage" */ './pages/WelcomePage.vue')
		},
		{
			path: '/:pathMatch(.*)*',
			name: '404',
			props: true,
			meta: {
				title: 'Page Not Found'
			},
			component: () =>
				import(/* webpackChunkName: "Delta404Page" */ './pages/Delta404Page.vue')
		},
		{
			path: '/not-authorized',
			name: '403',
			props: true,
			meta: {
				title: 'Not Authorized'
			},
			component: () =>
				import(/* webpackChunkName: "Delta403Page" */ './pages/Delta403Page.vue')
		}
	],
	// eslint-disable-next-line @typescript-eslint/no-unused-vars
	scrollBehavior(to, from, savedPosition) {
		if (to.hash) {
			return {
				selector: to.hash,
				offset: { x: 0, y: appLayoutStyles.topBarHeight }
			}
		}
		// The calendar explorer has a top section above the explorer content;
		// When filters are applied, the page should not jump to the top on the route change
		switch (to.name) {
			case RouteName.CalendarEvent:
			case RouteName.Calendar:
			case RouteName.AskTheExperts:
			case RouteName.AskTheExpertsDetails:
				return
			default:
				return { left: 0, top: 0 }
		}
	},
	parseQuery(query) {
		return qs.parse(query) as any
	},
	stringifyQuery(query) {
		const result = qs.stringify(query as any)

		return result ? result : ''
	}
})

// it's supposed to be used later
// @ts-ignore
// function insertRoutesIf(condition, ...routes) {
// 	return condition ? routes : []
// }

// Note: if we ever want to implement dynamic titles for some pages (e.g. with article names or rule names),
// we should simply export this helper to make sure all are prefixed consistently, and run after the initial Page API call completes
const addPrefixToTitle = (title: string) =>
	`AgencyIQ | ${namespaceTitle(DELTA_CONFIG.namespace)} | ${title}`

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
			document.title = `AgencyIQ | ${namespaceTitle(DELTA_CONFIG.namespace)}`
		}
	}
}

export const appendEntityTitleToPageTitle = (entityTitle: string) => {
	document.title = `${document.title} - ${entityTitle}`
}

const isExplorerInNewTabOrWindow = (to: RouteLocationNormalized): boolean => {
	const explorerEntityRoutes = [
		RouteName.CalendarEvent,
		RouteName.LegislationsDocument,
		RouteName.RegulationsDocument,
		RouteName.RegulationsRule,
		RouteName.StateRegulationRule
	]

	return !!(
		to.name &&
		explorerEntityRoutes.includes(to.name as RouteName) &&
		window.history.length === 1
	)
}

const redirectFromExplorerToEntityPage = (to: RouteLocationNormalized): RouteLocation => {
	let targetRouteName = to.name
	if (to.name === RouteName.CalendarEvent) targetRouteName = RouteName.Event
	if (to.name === RouteName.RegulationsDocument) targetRouteName = RouteName.Document
	if (to.name === RouteName.RegulationsRule) targetRouteName = RouteName.Rule
	if (to.name === RouteName.StateRegulationRule) targetRouteName = RouteName.StateRule
	if (to.name === RouteName.LegislationsDocument) targetRouteName = RouteName.Legislation

	return {
		...to,
		name: targetRouteName
	}
}

router.beforeEach(async (to, from, next) => {
	// TODO: Remove feature toggle when webinars goes live
	if (
		((to.name === RouteName.StateRegulations ||
			to.name === RouteName.StateRegulationRule) &&
			DELTA_CONFIG.namespace !== 'chemicalsiq') ||
		(to.name === RouteName.Webinars && !featEnabled(FeatureToggle.Webinars))
	) {
		next({ name: '404' })
	}

	addTitleToDocument(to)
	updateProfilingHeadersRouteUuid(to, from)

	// if we need body to be overflowed by some modal or component - add those to ModalsRoutes
	if (Object.values(ModalsRoutes).includes(to.name as any)) {
		document.body.classList.add('overflowHidden')
	} else {
		document.body.classList.remove('overflowHidden')
	}

	if (isExplorerInNewTabOrWindow(to)) {
		next(redirectFromExplorerToEntityPage(to))
	} else {
		next()
	}
})

export default router
