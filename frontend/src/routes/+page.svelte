<script lang="ts">
	import { onMount } from 'svelte';
	import { goto } from '$app/navigation';

	const accessTokenKey = 'accessToken';

	onMount(() => {
		const token = localStorage.getItem(accessTokenKey);
		goto(token ? '/tweets' : '/login');
	});
</script>

<main style="max-width: 900px; margin: 24px auto; padding: 0 16px; font-family: system-ui, sans-serif;">
	<p>Redirecting...</p>
</main>

