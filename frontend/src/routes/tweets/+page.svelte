<script lang="ts">
	import { onMount } from 'svelte';
	import { goto } from '$app/navigation';
	import type { BackendErrorResponse, FeedResponse, FeedTweetResponse } from '$lib/api/types';
	import { tokenHasAdminScope } from '$lib/api/jwt';

	let accessToken = $state<string | null>(null);
	let isAdmin = $state<boolean>(false);

	let feedLoading = $state<boolean>(false);
	let feedTweets = $state<FeedTweetResponse[]>([]);
	let page = $state<number>(0);
	let pageSize = $state<number>(10);
	let totalPages = $state<number>(0);

	let tweetPost = $state<string>('');
	let actionStatus = $state<string | null>(null);
	let errorMessage = $state<string | null>(null);

	let authMissing = $state<boolean>(false);

	function clearAuth() {
		accessToken = null;
		isAdmin = false;
		localStorage.removeItem('accessToken');
	}

	function getAuthHeader() {
		return accessToken ? `Bearer ${accessToken}` : '';
	}

	async function extractBackendError(res: Response) {
		const contentType = res.headers.get('content-type') ?? '';

		if (contentType.includes('application/json')) {
			const data = (await res.json().catch(() => null)) as BackendErrorResponse | null;
			return data?.message ?? data?.error ?? `Request failed (${res.status})`;
		}

		const text = await res.text().catch(() => '');
		return text || res.statusText || `Request failed (${res.status})`;
	}

	async function apiJson<T>(path: string, init?: RequestInit) {
		const res = await fetch(path, init);
		if (!res.ok) {
			const message = await extractBackendError(res);
			if (res.status === 401) {
				clearAuth();
				authMissing = true;
			}
			throw new Error(message);
		}
		return (await res.json()) as T;
	}

	async function apiNoBody(path: string, init?: RequestInit) {
		const res = await fetch(path, init);
		if (!res.ok) {
			const message = await extractBackendError(res);
			if (res.status === 401) {
				clearAuth();
				authMissing = true;
			}
			throw new Error(message);
		}
	}

	async function loadFeed(targetPage: number) {
		if (!accessToken) return;

		errorMessage = null;
		actionStatus = null;
		feedLoading = true;

		try {
			const data = await apiJson<FeedResponse>(
				`/api/tweets/feed?page=${targetPage}&pageSize=${pageSize}`,
				{ headers: { Authorization: getAuthHeader() } }
			);

			feedTweets = data.feedTweets;
			page = data.page;
			totalPages = data.totalPages;
		} catch (e) {
			errorMessage = e instanceof Error ? e.message : String(e);
		} finally {
			feedLoading = false;
		}
	}

	async function publishTweet() {
		if (!accessToken) return;

		errorMessage = null;
		actionStatus = null;

		const post = tweetPost.trim();
		if (!post) {
			errorMessage = 'Tweet text cannot be empty.';
			return;
		}

		try {
			await apiNoBody('/api/tweets/new', {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json',
					Authorization: getAuthHeader()
				},
				body: JSON.stringify({ post })
			});

			tweetPost = '';
			actionStatus = 'Tweet published.';
			await loadFeed(0);
		} catch (e) {
			errorMessage = e instanceof Error ? e.message : String(e);
		}
	}

	async function removeTweet(id: number) {
		if (!accessToken) return;

		errorMessage = null;
		actionStatus = null;

		try {
			await apiNoBody(`/api/tweets/delete/${id}`, {
				method: 'DELETE',
				headers: { Authorization: getAuthHeader() }
			});

			actionStatus = 'Tweet removed.';
			await loadFeed(page);
		} catch (e) {
			errorMessage = e instanceof Error ? e.message : String(e);
		}
	}

	onMount(async () => {
		const saved = localStorage.getItem('accessToken');
		if (!saved) {
			authMissing = true;
			return;
		}

		accessToken = saved;
		isAdmin = tokenHasAdminScope(saved);
		await loadFeed(0);
	});
</script>

<main style="max-width: 900px; margin: 24px auto; padding: 0 16px; font-family: system-ui, sans-serif;">
	<div style="display: flex; align-items: center; justify-content: space-between; gap: 12px; margin-bottom: 16px;">
		<h1 style="margin: 0;">Tweets</h1>
		<div style="display: flex; gap: 10px; align-items: center;">
			{#if authMissing}
				<button
					type="button"
					onclick={() => goto('/login')}
					style="padding: 10px 14px; border-radius: 8px; border: 1px solid #ccc; background: #fff; cursor: pointer;"
				>
					Sign in
				</button>
				<a href="/register" style="color: #0a66c2; text-decoration: none;">Criar Conta</a>
			{:else}
				<button
					type="button"
					onclick={() => {
						clearAuth();
						goto('/login');
					}}
					style="padding: 10px 14px; border-radius: 8px; border: 1px solid #ccc; background: #fff; cursor: pointer;"
				>
					Sair
				</button>

				{#if isAdmin}
					<button
						type="button"
						onclick={() => goto('/admin/users')}
						style="padding: 10px 14px; border-radius: 8px; border: 1px solid #ccc; background: #fff; cursor: pointer;"
					>
						Admin: Accounts
					</button>
				{/if}
			{/if}
		</div>
	</div>

	{#if errorMessage}
		<div style="background: #fee; border: 1px solid #f99; padding: 10px 12px; border-radius: 8px; margin-bottom: 16px;">
			{errorMessage}
		</div>
	{/if}

	{#if actionStatus}
		<div style="background: #efe; border: 1px solid #9f9; padding: 10px 12px; border-radius: 8px; margin-bottom: 16px;">
			{actionStatus}
		</div>
	{/if}

	{#if !authMissing}
		<section style="padding: 14px; border: 1px solid #ddd; border-radius: 12px; margin-bottom: 18px;">
			<h2 style="margin: 0 0 10px; font-size: 16px;">Faça um tweet</h2>

			<form
				onsubmit={(e) => {
					e.preventDefault();
					publishTweet();
				}}
				style="display: grid; gap: 10px;"
			>
				<label style="display: grid; gap: 6px;">
					<span>Tweet</span>
					<textarea
						rows="3"
						bind:value={tweetPost}
						placeholder="What's happening?"
						style="padding: 8px; border-radius: 8px; border: 1px solid #ccc; resize: vertical;"
					></textarea>
				</label>

				<div style="display: flex; gap: 10px; align-items: center;">
					<button
						type="submit"
						style="padding: 10px 14px; border-radius: 8px; border: none; background: #0a66c2; color: #fff; cursor: pointer;"
					>
						Publicar
					</button>

					<button
						type="button"
						disabled={feedLoading}
						onclick={() => loadFeed(0)}
						style="padding: 10px 14px; border-radius: 8px; border: none; background: #111; color: #fff; cursor: pointer;"
					>
						Carregar Feed
					</button>
				</div>
			</form>
		</section>

		<section style="padding: 14px; border: 1px solid #ddd; border-radius: 12px;">
			<h2 style="margin: 0 0 10px; font-size: 16px;">Feed</h2>

			<div style="display: flex; align-items: center; gap: 10px; margin-bottom: 14px;">
				<button
					type="button"
					disabled={page <= 0 || feedLoading}
					onclick={() => loadFeed(page - 1)}
					style="padding: 8px 12px; border-radius: 8px; border: 1px solid #ccc; background: #fff; cursor: pointer;"
				>
					Previous page
				</button>

				<span>
					Page <strong>{page}</strong> / <strong>{totalPages}</strong>
				</span>

				<button
					type="button"
					disabled={page >= totalPages - 1 || feedLoading}
					onclick={() => loadFeed(page + 1)}
					style="padding: 8px 12px; border-radius: 8px; border: 1px solid #ccc; background: #fff; cursor: pointer;"
				>
					Next page
				</button>

				<label style="margin-left: auto; display: grid; gap: 6px;">
					<span>Page size</span>
					<select
						value={pageSize}
						onchange={(e) => (pageSize = Number((e.currentTarget as HTMLSelectElement).value))}
						style="padding: 8px; border-radius: 8px; border: 1px solid #ccc;"
					>
						<option value={5}>5</option>
						<option value={10}>10</option>
						<option value={20}>20</option>
					</select>
				</label>
			</div>

			{#if feedLoading}
				<p>Carregando feed…</p>
			{:else if feedTweets.length === 0}
				<p>Sem novos tweets.</p>
			{:else}
				<ul style="list-style: none; padding: 0; margin: 0; display: grid; gap: 12px;">
					{#each feedTweets as t (t.id)}
						<li style="border: 1px solid #eee; border-radius: 12px; padding: 12px;">
							<div style="display: flex; justify-content: space-between; gap: 12px; align-items: start;">
								<div style="min-width: 0;">
									<div style="font-weight: 600; margin-bottom: 6px;">@{t.userName}</div>
									<div style="word-break: break-word;">{t.post}</div>
								</div>

								<button
									type="button"
									onclick={() => removeTweet(t.id)}
									style="padding: 8px 12px; border-radius: 8px; border: 1px solid #e99; background: #fff; cursor: pointer; color: #b00;"
								>
									Deletar tweet
								</button>
							</div>
						</li>
					{/each}
				</ul>
			{/if}
		</section>
	{/if}
</main>

