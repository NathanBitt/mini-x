<script lang="ts">
	import { onMount } from 'svelte';
	import { goto } from '$app/navigation';
	import type { BackendErrorResponse, UserResponse } from '$lib/api/types';
	import { tokenHasAdminScope } from '$lib/api/jwt';

	let accessToken = $state<string | null>(null);
	let isAdmin = $state<boolean>(false);

	let loading = $state<boolean>(false);
	let users = $state<UserResponse[]>([]);
	let pageError = $state<string | null>(null);

	const accessTokenKey = 'accessToken';

	function clearAuth() {
		accessToken = null;
		localStorage.removeItem(accessTokenKey);
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
			throw new Error(message);
		}
		return (await res.json()) as T;
	}

	async function loadUsers() {
		if (!accessToken) return;

		pageError = null;
		loading = true;
		users = [];

		try {
			const data = await apiJson<UserResponse[]>('/api/users/list', {
				method: 'GET',
				headers: { Authorization: getAuthHeader() }
			});
			users = data;
		} catch (e) {
			const msg = e instanceof Error ? e.message : String(e);
			// Backend uses Spring Security; non-admins typically get 403.
			pageError = msg.toLowerCase().includes('forbidden') || msg.toLowerCase().includes('403') ? 'Admin only.' : msg;
		} finally {
			loading = false;
		}
	}

	onMount(() => {
		const saved = localStorage.getItem(accessTokenKey);
		if (!saved) {
			goto('/login');
			return;
		}

		accessToken = saved;
		isAdmin = tokenHasAdminScope(saved);
		loadUsers();
	});
</script>

<main style="max-width: 1100px; margin: 24px auto; padding: 0 16px; font-family: system-ui, sans-serif;">
	<div style="display: flex; align-items: center; justify-content: space-between; gap: 12px; margin-bottom: 16px;">
		<div style="display: flex; align-items: center; gap: 12px;">
			<button
				type="button"
				onclick={() => goto('/tweets')}
				style="padding: 10px 14px; border-radius: 8px; border: 1px solid #ccc; background: #fff; cursor: pointer;"
			>
				Tweets
			</button>

			<h1 style="margin: 0; font-size: 20px;">Admin: Accounts</h1>
		</div>

		<button
			type="button"
			onclick={() => {
				clearAuth();
				goto('/login');
			}}
			style="padding: 10px 14px; border-radius: 8px; border: 1px solid #ccc; background: #fff; cursor: pointer;"
		>
			Sign out
		</button>
	</div>

	{#if pageError}
		<div style="background: #fee; border: 1px solid #f99; padding: 10px 12px; border-radius: 8px; margin-bottom: 16px;">
			{pageError}
		</div>
	{/if}

	{#if !isAdmin}
		<div style="background: #fff7e6; border: 1px solid #ffd166; padding: 10px 12px; border-radius: 8px; margin-bottom: 16px;">
			Your account does not appear to be an admin.
		</div>
	{/if}

	<section style="padding: 14px; border: 1px solid #ddd; border-radius: 12px;">
		{#if loading}
			<p>Loading accounts...</p>
		{:else if users.length === 0}
			<p>No accounts found.</p>
		{:else}
			<table style="width: 100%; border-collapse: collapse;">
				<thead>
					<tr>
						<th style="text-align: left; padding: 10px; border-bottom: 1px solid #eee;">Username</th>
						<th style="text-align: left; padding: 10px; border-bottom: 1px solid #eee;">Roles</th>
					</tr>
				</thead>
				<tbody>
					{#each users as u (u.id)}
						<tr>
							<td style="padding: 10px; border-bottom: 1px solid #f5f5f5;">{u.userName}</td>
							<td style="padding: 10px; border-bottom: 1px solid #f5f5f5;">
								{#if u.roles?.length}
									{u.roles.map((r) => r.name).join(', ')}
								{:else}
									<em style="color:#777;">none</em>
								{/if}
							</td>
						</tr>
					{/each}
				</tbody>
			</table>
		{/if}
	</section>
</main>

