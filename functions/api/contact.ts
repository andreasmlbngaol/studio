interface Env {
  RESEND_API_KEY?: string;
  CONTACT_TO?: string;
  CONTACT_FROM?: string;
}

interface ContactPayload {
  name?: string;
  email?: string;
  message?: string;
}

const jsonHeaders = { "Content-Type": "application/json" };

function json(body: unknown, status: number): Response {
  return new Response(JSON.stringify(body), { status, headers: jsonHeaders });
}

function isValidEmail(value: string): boolean {
  return /^[^@\s]+@[^@\s]+\.[^@\s]+$/.test(value);
}

export const onRequestPost: PagesFunction<Env> = async (context) => {
  let payload: ContactPayload;
  try {
    payload = await context.request.json<ContactPayload>();
  } catch {
    return json({ ok: false, error: "invalid_json" }, 400);
  }

  const name = (payload.name ?? "").trim();
  const email = (payload.email ?? "").trim();
  const message = (payload.message ?? "").trim();

  if (name.length < 2 || name.length > 120) {
    return json({ ok: false, error: "invalid_name" }, 400);
  }
  if (!isValidEmail(email) || email.length > 200) {
    return json({ ok: false, error: "invalid_email" }, 400);
  }
  if (message.length < 10 || message.length > 5000) {
    return json({ ok: false, error: "invalid_message" }, 400);
  }

  const apiKey = context.env.RESEND_API_KEY;
  const to = context.env.CONTACT_TO || "lgandre45@gmail.com";
  const from = context.env.CONTACT_FROM || "Studio <onboarding@resend.dev>";

  if (!apiKey) {
    return json({ ok: false, error: "not_configured" }, 503);
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
    return json({ ok: false, error: "send_failed" }, 502);
  }

  return json({ ok: true }, 200);
};

export const onRequestOptions: PagesFunction = async () =>
  new Response(null, {
    status: 204,
    headers: {
      "Access-Control-Allow-Methods": "POST, OPTIONS",
      "Access-Control-Allow-Headers": "Content-Type",
    },
  });
