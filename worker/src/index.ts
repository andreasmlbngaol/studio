interface Env {
  RESEND_API_KEY?: string;
  CONTACT_TO?: string;
  CONTACT_FROM?: string;
  ALLOWED_ORIGINS?: string;
}

interface ContactPayload {
  name?: string;
  email?: string;
  message?: string;
}

const DEFAULT_TO = "lgandre45@gmail.com";
const DEFAULT_FROM = "Studio <onboarding@resend.dev>";
const jsonHeaders = { "Content-Type": "application/json" };

function json(body: unknown, status: number, cors: Record<string, string>): Response {
  return new Response(JSON.stringify(body), {
    status,
    headers: { ...jsonHeaders, ...cors },
  });
}

function corsHeaders(request: Request, env: Env): Record<string, string> {
  const origin = request.headers.get("Origin");
  const configured = (env.ALLOWED_ORIGINS ?? "*")
    .split(",")
    .map((value) => value.trim())
    .filter(Boolean);
  const allow = configured.includes("*")
    ? "*"
    : origin && configured.includes(origin)
      ? origin
      : configured[0] ?? "";
  return {
    "Access-Control-Allow-Origin": allow,
    "Access-Control-Allow-Methods": "POST, OPTIONS",
    "Access-Control-Allow-Headers": "Content-Type",
    "Access-Control-Max-Age": "86400",
    Vary: "Origin",
  };
}

function isValidEmail(value: string): boolean {
  return /^[^@\s]+@[^@\s]+\.[^@\s]+$/.test(value);
}

export default {
  async fetch(request: Request, env: Env): Promise<Response> {
    const cors = corsHeaders(request, env);

    if (request.method === "OPTIONS") {
      return new Response(null, { status: 204, headers: cors });
    }

    const path = new URL(request.url).pathname;
    if (request.method !== "POST" || (path !== "/" && path !== "/api/contact")) {
      return json({ ok: false, error: "not_found" }, 404, cors);
    }

    let payload: ContactPayload;
    try {
      payload = (await request.json()) as ContactPayload;
    } catch {
      return json({ ok: false, error: "invalid_json" }, 400, cors);
    }

    const name = (payload.name ?? "").trim();
    const email = (payload.email ?? "").trim();
    const message = (payload.message ?? "").trim();

    if (name.length < 2 || name.length > 120) {
      return json({ ok: false, error: "invalid_name" }, 400, cors);
    }
    if (!isValidEmail(email) || email.length > 200) {
      return json({ ok: false, error: "invalid_email" }, 400, cors);
    }
    if (message.length < 10 || message.length > 5000) {
      return json({ ok: false, error: "invalid_message" }, 400, cors);
    }

    const apiKey = env.RESEND_API_KEY;
    const to = env.CONTACT_TO || DEFAULT_TO;
    const from = env.CONTACT_FROM || DEFAULT_FROM;

    if (!apiKey) {
      return json({ ok: false, error: "not_configured" }, 503, cors);
    }

    const response = await fetch("https://api.resend.com/emails", {
      method: "POST",
      headers: {
        Authorization: `Bearer ${apiKey}`,
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        from,
        to: [to],
        reply_to: email,
        subject: `Portfolio contact from ${name}`,
        text: `${message}\n\nFrom: ${name} <${email}>`,
      }),
    });

    if (!response.ok) {
      return json({ ok: false, error: "send_failed" }, 502, cors);
    }

    return json({ ok: true }, 200, cors);
  },
};
